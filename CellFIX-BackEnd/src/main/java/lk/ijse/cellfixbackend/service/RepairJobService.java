package lk.ijse.cellfixbackend.service;

import lk.ijse.cellfixbackend.dto.RepairJobDTO;
import lk.ijse.cellfixbackend.dto.RepairJobResponseDTO;
import lk.ijse.cellfixbackend.entity.RepairJob;

import java.io.IOException;
import java.util.List;

public interface RepairJobService {
    RepairJob assignRepairJob(RepairJobDTO repairJobDTO) throws IOException;
    RepairJob completeRepairJob(int repairJobId);
    void updateRepairJobStatus(int jobId, String status);
    List<RepairJobResponseDTO> getAllRepairJobs();
    void deleteRepairJob(int repairJobId);

}
