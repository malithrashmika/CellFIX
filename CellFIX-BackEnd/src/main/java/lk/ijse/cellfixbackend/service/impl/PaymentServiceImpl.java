package lk.ijse.cellfixbackend.service.impl;

import lk.ijse.cellfixbackend.dto.PaymentDTO;
import lk.ijse.cellfixbackend.entity.Invoice;
import lk.ijse.cellfixbackend.entity.Payment;
import lk.ijse.cellfixbackend.repo.InvoiceRepo;
import lk.ijse.cellfixbackend.repo.PaymentRepo;
import lk.ijse.cellfixbackend.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private InvoiceRepo invoiceRepo;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public Payment addPayment(PaymentDTO paymentDTO) {
        Optional<Invoice> invoiceOptional = invoiceRepo.findById(paymentDTO.getInvoiceId());
        if (invoiceOptional.isPresent()) {
            Invoice invoice = invoiceOptional.get();

            Payment payment = modelMapper.map(paymentDTO, Payment.class);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setInvoice(invoice);

            return paymentRepo.save(payment);
        } else {
            throw new RuntimeException("Invoice not found for ID: " + paymentDTO.getInvoiceId());
        }
    }

}
