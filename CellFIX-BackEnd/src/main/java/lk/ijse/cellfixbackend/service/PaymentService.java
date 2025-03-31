package lk.ijse.cellfixbackend.service;

import lk.ijse.cellfixbackend.dto.PaymentDTO;
import lk.ijse.cellfixbackend.entity.Payment;

public interface PaymentService {

    Payment addPayment(PaymentDTO paymentDTO);
}
