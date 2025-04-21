package lk.ijse.cellfixbackend.repo;

import lk.ijse.cellfixbackend.entity.RepairInventory;
import lk.ijse.cellfixbackend.entity.RepairJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairInventoryRepo extends JpaRepository<RepairInventory,Integer > {
    List<RepairInventory> findByRepairJob(RepairJob repairJob);
    List<RepairInventory> findByRepairJobId(int repairJobId);

}
