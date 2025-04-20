package lk.ijse.cellfixbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lk.ijse.cellfixbackend.dto.CustomerDTO;
import lk.ijse.cellfixbackend.entity.Customer;
import lk.ijse.cellfixbackend.exception.AlreadyExistsException;
import lk.ijse.cellfixbackend.exception.NotFoundException;
import lk.ijse.cellfixbackend.repo.CustomerRepo;
import lk.ijse.cellfixbackend.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Customer save(CustomerDTO customerDTO) {


        if (customerDTO.getPhoneNumber() != null) {
            if (customerRepo.existsByPhoneNumber(customerDTO.getPhoneNumber())) {
                throw new AlreadyExistsException("Customer with phone number " + customerDTO.getPhoneNumber() + " already exists.");
            }
        } else {
            throw new IllegalArgumentException("Phone number cannot be null");
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
                .orElseThrow(() -> new NotFoundException("Customer with phone number " + phoneNumber + " not found."));

        // Delete customer
        customerRepo.delete(customer);
    }

    @Override
    public List<CustomerDTO> findAll() {
        List<Customer> customers = customerRepo.findAll();

        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }

    public List<String> getAllPhoneNumbers() {
        return customerRepo.findAll()
                .stream()
                .map(Customer::getPhoneNumber)
                .collect(Collectors.toList());
    }


}
