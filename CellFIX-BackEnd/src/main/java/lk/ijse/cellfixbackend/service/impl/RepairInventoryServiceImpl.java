package lk.ijse.cellfixbackend.service.impl;

import lk.ijse.cellfixbackend.dto.RepairInventoryDTO;
import lk.ijse.cellfixbackend.entity.Inventory;
import lk.ijse.cellfixbackend.entity.RepairInventory;
import lk.ijse.cellfixbackend.entity.RepairJob;
import lk.ijse.cellfixbackend.repo.InventoryRepo;
import lk.ijse.cellfixbackend.repo.RepairInventoryRepo;
import lk.ijse.cellfixbackend.repo.RepairJobRepo;
import lk.ijse.cellfixbackend.service.RepairInventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepairInventoryServiceImpl implements RepairInventoryService {

    @Autowired
    private RepairInventoryRepo repairInventoryRepo;
    @Autowired
    private RepairJobRepo repairJobRepo;
    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RepairInventoryDTO addRepairInventory(RepairInventoryDTO repairInventoryDTO) {
        RepairJob repairJob = repairJobRepo.findById(repairInventoryDTO.getRepairId())
                .orElseThrow(() -> new RuntimeException("Repair Job Not Found"));

        Inventory inventory = inventoryRepo.findById(repairInventoryDTO.getInventoryId())
                .orElseThrow(() -> new RuntimeException("Inventory Item Not Found"));

        RepairInventory repairInventory = new RepairInventory();
        repairInventory.setRepairJob(repairJob);
        repairInventory.setInventory(inventory);
        repairInventory.setQuantityUsed(repairInventoryDTO.getQuantityUsed());
        repairInventory.setRepairStatus(repairInventoryDTO.getRepairStatus());

        RepairInventory savedRepairInventory = repairInventoryRepo.save(repairInventory);
        return modelMapper.map(savedRepairInventory, RepairInventoryDTO.class);
    }
}

