package lk.ijse.cellfixbackend.repo;

import lk.ijse.cellfixbackend.entity.Customer;
import lk.ijse.cellfixbackend.entity.RepairJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepairJobRepo extends JpaRepository<RepairJob, Integer> {
    Optional<RepairJob> findById(int id);
    Optional<RepairJob> findByCustomerPhoneNumber(String phone);
    List<RepairJob> findAllByCustomer(Customer customer);


}
