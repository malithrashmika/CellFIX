package lk.ijse.cellfixbackend.controller;

import lk.ijse.cellfixbackend.dto.CustomerDTO;
import lk.ijse.cellfixbackend.dto.ResponseDTO;
import lk.ijse.cellfixbackend.entity.Customer;
import lk.ijse.cellfixbackend.exception.AlreadyExistsException;
import lk.ijse.cellfixbackend.exception.NotFoundException;
import lk.ijse.cellfixbackend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/customer")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ResponseDTO responseDTO;

    @PostMapping("/save")
    public ResponseEntity<?> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            Customer savedCustomer = customerService.save(customerDTO);
            return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update/{phoneNumber}")
    public ResponseEntity<?> updateCustomer(@PathVariable String phoneNumber, @RequestBody CustomerDTO customerDTO) {
        Customer updatedCustomer = customerService.update(phoneNumber, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }
    @DeleteMapping("/delete/{phoneNumber}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String phoneNumber) {
        try {
            customerService.delete(phoneNumber);
            return new ResponseEntity<>("Customer deleted successfully.", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
