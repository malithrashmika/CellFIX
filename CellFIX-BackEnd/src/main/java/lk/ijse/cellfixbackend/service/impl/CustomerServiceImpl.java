package lk.ijse.cellfixbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lk.ijse.cellfixbackend.dto.CustomerDTO;
import lk.ijse.cellfixbackend.entity.Customer;
import lk.ijse.cellfixbackend.exception.CustomerAlreadyExistsException;
import lk.ijse.cellfixbackend.exception.CustomerNotFoundException;
import lk.ijse.cellfixbackend.repo.CustomerRepo;
import lk.ijse.cellfixbackend.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Customer save(CustomerDTO customerDTO) {
        if (customerRepo.existsById(Integer.valueOf(customerDTO.getPhoneNumber()))) {
            throw new CustomerAlreadyExistsException("Customer with phone number " + customerDTO.getPhoneNumber() + " already exists.");
        }

        // Convert DTO to entity
        Customer customer = modelMapper.map(customerDTO, Customer.class);

        // Save the customer
        return customerRepo.save(customer);

    }
   @Override
    public Customer update(String phoneNumber, CustomerDTO customerDTO) {
        // Check if the customer exists
        Customer existingCustomer = customerRepo.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with phone number: " + phoneNumber));

        // Update fields if provided
        if (customerDTO.getName() != null) {
            existingCustomer.setName(customerDTO.getName());
        }
        if (customerDTO.getEmail() != null) {
            existingCustomer.setEmail(customerDTO.getEmail());
        }
        if (customerDTO.getAddress() != null) {
            existingCustomer.setAddress(customerDTO.getAddress());
        }

        // Save updated customer
        return customerRepo.save(existingCustomer);
    }

    @Override
    public void delete(String phoneNumber) {
        Customer customer = customerRepo.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with phone number " + phoneNumber + " not found."));

        // Delete customer
        customerRepo.delete(customer);
    }

    @Override
    public List<CustomerDTO> findAll() {
        return List.of();
    }

}
