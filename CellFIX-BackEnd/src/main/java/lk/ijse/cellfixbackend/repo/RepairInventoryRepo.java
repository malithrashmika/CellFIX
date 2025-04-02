package lk.ijse.cellfixbackend.repo;

import lk.ijse.cellfixbackend.entity.RepairInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairInventoryRepo extends JpaRepository<RepairInventory, String> {

}
