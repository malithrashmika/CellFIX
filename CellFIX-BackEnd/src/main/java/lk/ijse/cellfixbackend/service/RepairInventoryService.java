package lk.ijse.cellfixbackend.service;

import lk.ijse.cellfixbackend.dto.RepairInventoryDTO;
import lk.ijse.cellfixbackend.entity.RepairJob;

import java.util.List;

public interface RepairInventoryService {
    RepairInventoryDTO addRepairInventory(RepairInventoryDTO repairInventoryDTO);
    RepairJob completeRepairJobWithParts(int repairJobId, List<RepairInventoryDTO> usedPartsDTOs);
}
