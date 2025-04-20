package lk.ijse.cellfixbackend.service.impl;

import lk.ijse.cellfixbackend.dto.RepairInventoryDTO;
import lk.ijse.cellfixbackend.entity.Inventory;
import lk.ijse.cellfixbackend.entity.Invoice;
import lk.ijse.cellfixbackend.entity.RepairInventory;
import lk.ijse.cellfixbackend.entity.RepairJob;
import lk.ijse.cellfixbackend.repo.InventoryRepo;
import lk.ijse.cellfixbackend.repo.InvoiceRepo;
import lk.ijse.cellfixbackend.repo.RepairInventoryRepo;
import lk.ijse.cellfixbackend.repo.RepairJobRepo;
import lk.ijse.cellfixbackend.service.RepairInventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RepairInventoryServiceImpl implements RepairInventoryService {

    @Autowired
    private RepairInventoryRepo repairInventoryRepo;
    @Autowired
    private RepairJobRepo repairJobRepo;
    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private InvoiceRepo invoiceRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RepairInventoryDTO addRepairInventory(RepairInventoryDTO repairInventoryDTO) {
        RepairJob repairJob = repairJobRepo.findById(repairInventoryDTO.getRepairId())
                .orElseThrow(() -> new RuntimeException("Repair Job Not Found"));

        Inventory inventory = inventoryRepo.findById(repairInventoryDTO.getInventoryId())
                .orElseThrow(() -> new RuntimeException("Inventory Item Not Found"));

        // Deduct stock immediately when assigning parts
        int newStock = inventory.getStockQuantity() - repairInventoryDTO.getQuantityUsed();
        if (newStock < 0) {
            throw new RuntimeException("Not enough stock for part: " + inventory.getPartName());
        }
        inventory.setStockQuantity(newStock);
        inventoryRepo.save(inventory);

        RepairInventory repairInventory = new RepairInventory();
        repairInventory.setRepairJob(repairJob);
        repairInventory.setInventory(inventory);
        repairInventory.setQuantityUsed(repairInventoryDTO.getQuantityUsed());
        repairInventory.setRepairStatus(repairInventoryDTO.getRepairStatus());

        RepairInventory savedRepairInventory = repairInventoryRepo.save(repairInventory);
        return modelMapper.map(savedRepairInventory, RepairInventoryDTO.class);
    }

    @Transactional
    public RepairJob completeRepairJobWithParts(int repairJobId, List<RepairInventoryDTO> usedPartsDTOs) {
        RepairJob repairJob = repairJobRepo.findById(repairJobId)
                .orElseThrow(() -> new RuntimeException("Repair Job not found"));

        if (!repairJob.getStatus().equalsIgnoreCase("Pending")) {
            throw new RuntimeException("Repair job already completed or in an invalid state.");
        }

        double totalCost = 0;

        for (RepairInventoryDTO dto : usedPartsDTOs) {
            Inventory inventory = inventoryRepo.findById(dto.getInventoryId())
                    .orElseThrow(() -> new RuntimeException("Inventory Item Not Found"));

            if (inventory.getStockQuantity() < dto.getQuantityUsed()) {
                throw new RuntimeException("Insufficient stock for part: " + inventory.getPartName());
            }

            // Deduct stock
            inventory.setStockQuantity(inventory.getStockQuantity() - dto.getQuantityUsed());
            inventoryRepo.save(inventory);

            // Save repair inventory
            RepairInventory repairInventory = new RepairInventory();
            repairInventory.setRepairJob(repairJob);
            repairInventory.setInventory(inventory);
            repairInventory.setQuantityUsed(dto.getQuantityUsed());
            repairInventory.setRepairStatus("Completed");
            repairInventoryRepo.save(repairInventory);

            // Add to total cost
            totalCost += inventory.getPrice() * dto.getQuantityUsed();
        }

        // Create Invoice
        Invoice invoice = new Invoice();
        invoice.setRepairJob(repairJob);
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setTotalAmount(totalCost);
        invoiceRepo.save(invoice);

        // Link invoice & update status
        repairJob.setInvoice(invoice);
        repairJob.setStatus("Completed");

        return repairJobRepo.save(repairJob);
    }


}

