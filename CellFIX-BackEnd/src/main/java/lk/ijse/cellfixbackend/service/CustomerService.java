package lk.ijse.cellfixbackend.service;

import lk.ijse.cellfixbackend.dto.CustomerDTO;
import lk.ijse.cellfixbackend.entity.Customer;

import java.util.List;

public interface CustomerService {
   Customer save(CustomerDTO customerDTO);
    Customer update(String phoneNumber, CustomerDTO customerDTO);
    void delete(String phoneNumber);
    List<CustomerDTO> findAll();
    List<String> getAllPhoneNumbers();
}
