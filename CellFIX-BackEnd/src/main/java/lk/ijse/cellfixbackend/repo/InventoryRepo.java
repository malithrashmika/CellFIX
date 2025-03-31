package lk.ijse.cellfixbackend.repo;

import lk.ijse.cellfixbackend.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Integer> {
    Optional<Inventory> existsById(int id);
}
