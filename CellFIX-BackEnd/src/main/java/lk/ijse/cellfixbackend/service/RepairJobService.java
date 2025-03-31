package lk.ijse.cellfixbackend.service;

import lk.ijse.cellfixbackend.dto.RepairJobDTO;
import lk.ijse.cellfixbackend.entity.RepairJob;

import java.io.IOException;

public interface RepairJobService {
    RepairJob assignRepairJob(RepairJobDTO repairJobDTO) throws IOException;
    RepairJob completeRepairJob(int repairJobId);
}
