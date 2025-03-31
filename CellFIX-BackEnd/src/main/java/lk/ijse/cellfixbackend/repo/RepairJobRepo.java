package lk.ijse.cellfixbackend.repo;

import lk.ijse.cellfixbackend.entity.RepairJob;
import lk.ijse.cellfixbackend.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepairJobRepo extends JpaRepository<RepairJob, Integer> {
    Optional<RepairJob> findById(int id);
}
