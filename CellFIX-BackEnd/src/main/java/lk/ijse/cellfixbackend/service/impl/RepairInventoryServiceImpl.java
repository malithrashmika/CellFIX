package lk.ijse.cellfixbackend.service.impl;

import lk.ijse.cellfixbackend.dto.RepairInventoryDTO;
import lk.ijse.cellfixbackend.dto.RepairJobDTO;
import lk.ijse.cellfixbackend.entity.*;
import lk.ijse.cellfixbackend.repo.*;
import lk.ijse.cellfixbackend.service.InvoiceService;
import lk.ijse.cellfixbackend.service.Invoice_Genarator;
import lk.ijse.cellfixbackend.service.RepairInventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepairInventoryServiceImpl implements RepairInventoryService {

    @Autowired
    private RepairInventoryRepo repairInventoryRepo;
    @Autowired
    private Invoice_Genarator invoiceGenarator;
    @Autowired
    private RepairJobRepo repairJobRepo;
    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private CustomerRepo customerRepo;

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

//    @Transactional
//    public RepairJob completeRepairJobWithParts(int repairJobId, List<RepairInventoryDTO> usedPartsDTOs) {
//        RepairJob repairJob = repairJobRepo.findById(repairJobId)
//                .orElseThrow(() -> new RuntimeException("Repair Job not found"));
//
//        if (!repairJob.getStatus().equalsIgnoreCase("Pending")) {
//            throw new RuntimeException("Repair job already completed or in an invalid state.");
//        }
//
//        double totalCost = 0;
//
//        for (RepairInventoryDTO dto : usedPartsDTOs) {
//            Inventory inventory = inventoryRepo.findById(dto.getInventoryId())
//                    .orElseThrow(() -> new RuntimeException("Inventory Item Not Found"));
//
//            if (inventory.getStockQuantity() < dto.getQuantityUsed()) {
//                throw new RuntimeException("Insufficient stock for part: " + inventory.getPartName());
//            }
//
//            // Deduct stock
//            inventory.setStockQuantity(inventory.getStockQuantity() - dto.getQuantityUsed());
//            inventoryRepo.save(inventory);
//
//            // Save repair inventory
//            RepairInventory repairInventory = new RepairInventory();
//            repairInventory.setRepairJob(repairJob);
//            repairInventory.setInventory(inventory);
//            repairInventory.setQuantityUsed(dto.getQuantityUsed());
//            repairInventory.setRepairStatus("Completed");
//            repairInventoryRepo.save(repairInventory);
//
//            // Add to total cost
//            totalCost += inventory.getPrice() * dto.getQuantityUsed();
//        }
//
//        // Create Invoice
//        Invoice invoice = new Invoice();
//        invoice.setRepairJob(repairJob);
//        invoice.setInvoiceDate(LocalDateTime.now());
//        invoice.setTotalAmount(totalCost);
//        invoiceRepo.save(invoice);
//
//        // Link invoice & update status
//        repairJob.setInvoice(invoice);
//        repairJob.setStatus("Completed");
//
//        return repairJobRepo.save(repairJob);
//    }

    @Override
    public List<RepairInventoryDTO> getPartsByRepairJobId(int repairJobId) {
        RepairJob repairJob = repairJobRepo.findById(repairJobId)
                .orElseThrow(() -> new RuntimeException("Repair job not found"));

        List<RepairInventory> inventoryList = repairInventoryRepo.findByRepairJob(repairJob);

        return inventoryList.stream().map(inventory -> {
            RepairInventoryDTO dto = new RepairInventoryDTO();
            dto.setId(inventory.getId());
            dto.setPartName(inventory.getInventory().getPartName());
            dto.setUnitPrice(inventory.getInventory().getPrice());
            dto. setQuantityUsed(inventory.getQuantityUsed());
            return dto;
        }).collect(Collectors.toList());
    }


    @Autowired
    private InvoiceService invoiceService;

    @Override
    public void completeRepairAndGenerateInvoice(int repairId, List<RepairInventoryDTO> partsUsed) throws IOException {
        RepairJob repairJob = repairJobRepo.findById(repairId)
                .orElseThrow(() -> new RuntimeException("Repair job not found"));


        repairJob.setStatus("Completed");
        repairJobRepo.save(repairJob); // Don't forget to persist the change!


        List<RepairInventory> savedParts = saveRepairInventory(repairJob, partsUsed);


        double totalCost = calculateTotalCost(savedParts);


        Invoice savedInvoice = saveInvoice(repairJob, totalCost);

        byte[] pdf = invoiceGenarator.generateInvoicePdf(repairId);
        saveInvoicePdf(savedInvoice, pdf);
    }


    private List<RepairInventory> saveRepairInventory(RepairJob repairJob, List<RepairInventoryDTO> partsUsed) {
        List<RepairInventory> savedParts = new ArrayList<>();
        for (RepairInventoryDTO dto : partsUsed) {
            Inventory inventory = inventoryRepo.findById(dto.getInventoryId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found: " + dto.getInventoryId()));

            RepairInventory entity = new RepairInventory();
            entity.setRepairJob(repairJob); // Corrected
            entity.setInventory(inventory);
            entity.setQuantityUsed(dto.getQuantityUsed());
            entity.setRepairStatus(dto.getRepairStatus());

            savedParts.add(repairInventoryRepo.save(entity));
        }
        return savedParts;
    }


    private double calculateTotalCost(List<RepairInventory> savedParts) {
        return savedParts.stream()
                .mapToDouble(part -> part.getQuantityUsed() * part.getInventory().getPrice())
                .sum();
    }

    private Invoice saveInvoice(RepairJob repairJob, double totalCost) {
        Invoice invoice = new Invoice();
        invoice.setRepairJob(repairJob);
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setTotalAmount(totalCost);
        invoice.setCustomerName(repairJob.getCustomer().getName());
        invoice.setDeviceModel(repairJob.getDeviceModel());

        return invoiceRepo.save(invoice);
    }

    private void saveInvoicePdf(Invoice savedInvoice, byte[] pdf) throws IOException {
        Files.createDirectories(Paths.get("invoices"));
        Files.write(Paths.get("invoices/" + savedInvoice.getInvoiceId() + ".pdf"), pdf);
    }

    @Override
    @Transactional
    public void cancelRepairJob(int repairJobId) {
        RepairInventory repairInventory = repairInventoryRepo.findById(repairJobId)
                .orElseThrow(() -> new RuntimeException("Repair job not found with ID: " + repairJobId));

        String status = repairInventory.getRepairStatus();
        if ("Completed".equalsIgnoreCase(status)) {
            throw new RuntimeException("Completed repair jobs cannot be cancelled.");
        }
        if ("Cancelled".equalsIgnoreCase(status)) {
            throw new RuntimeException("Repair job is already cancelled.");
        }

        repairInventory.setRepairStatus("Cancelled");
        repairInventoryRepo.save(repairInventory);
    }

    @Override
    public List<RepairJobDTO> getRepairStatusByPhone(String phone) {
        Customer customer = customerRepo.findByPhoneNumber(phone)
                .orElseThrow(() -> new RuntimeException("Customer not found with phone number: " + phone));

        List<RepairJob> repairJobs = repairJobRepo.findAllByCustomer(customer);

        if (repairJobs.isEmpty()) {
            throw new RuntimeException("No repair jobs found for this customer");
        }

        return repairJobs.stream()
                .map(job -> modelMapper.map(job, RepairJobDTO.class))
                .collect(Collectors.toList());
    }





}

