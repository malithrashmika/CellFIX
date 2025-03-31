package lk.ijse.cellfixbackend.repo;

import lk.ijse.cellfixbackend.entity.Customer;
import lk.ijse.cellfixbackend.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechnicianRepo extends JpaRepository<Technician, Integer> {
    Optional existsById(int id);

    Optional<Technician> findById(int id);
}
