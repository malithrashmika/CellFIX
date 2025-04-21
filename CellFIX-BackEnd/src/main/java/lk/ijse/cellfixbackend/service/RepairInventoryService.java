package lk.ijse.cellfixbackend.service;

import lk.ijse.cellfixbackend.dto.RepairInventoryDTO;
import lk.ijse.cellfixbackend.dto.RepairJobDTO;

import java.io.IOException;
import java.util.List;

public interface RepairInventoryService {
    RepairInventoryDTO addRepairInventory(RepairInventoryDTO repairInventoryDTO);
    void cancelRepairJob(int id);
    List<RepairInventoryDTO> getPartsByRepairJobId(int repairJobId);
    void completeRepairAndGenerateInvoice(int repairId, List<RepairInventoryDTO> partsUsed) throws IOException, IOException;
    List<RepairJobDTO> getRepairStatusByPhone(String phone);
}
