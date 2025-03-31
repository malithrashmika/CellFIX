package lk.ijse.cellfixbackend.repo;

import lk.ijse.cellfixbackend.entity.Invoice;
import lk.ijse.cellfixbackend.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Integer> {
    Optional<Invoice> findById(int id);
}
