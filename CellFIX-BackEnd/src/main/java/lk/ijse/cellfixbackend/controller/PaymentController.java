package lk.ijse.cellfixbackend.controller;

import lk.ijse.cellfixbackend.dto.PaymentDTO;
import lk.ijse.cellfixbackend.entity.Payment;
import lk.ijse.cellfixbackend.exception.AlreadyExistsException;
import lk.ijse.cellfixbackend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @PostMapping("/add")
    public ResponseEntity<?> addPayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            Payment savedpayment = paymentService.addPayment(paymentDTO);
            return new ResponseEntity<>(savedpayment, HttpStatus.CREATED);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
