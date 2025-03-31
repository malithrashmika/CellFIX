package lk.ijse.cellfixbackend.service.impl;

import lk.ijse.cellfixbackend.dto.InvoiceDTO;
import lk.ijse.cellfixbackend.entity.Invoice;
import lk.ijse.cellfixbackend.entity.RepairJob;
import lk.ijse.cellfixbackend.repo.InvoiceRepo;
import lk.ijse.cellfixbackend.repo.RepairJobRepo;
import lk.ijse.cellfixbackend.service.InvoiceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepo invoiceRepo;

    @Autowired
    private RepairJobRepo repairJobRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        // Fetching the repair job for the invoice
        Optional<RepairJob> repairJob = repairJobRepo.findById(invoiceDTO.getRepairJobId());
    
        if (repairJob.isPresent()) {
            Invoice invoice = modelMapper.map(invoiceDTO, Invoice.class);
            invoice.setRepairJob(repairJob.get());
            invoice.setInvoiceDate(LocalDateTime.now());
    
            Invoice savedInvoice = invoiceRepo.save(invoice);
            return modelMapper.map(savedInvoice, InvoiceDTO.class);
        }
    
        throw new IllegalArgumentException("Repair job not found for the given ID: " + invoiceDTO.getRepairJobId());
    }

    @Override
    public InvoiceDTO getInvoiceById(int invoiceId) {
        Optional<Invoice> invoice = invoiceRepo.findById(invoiceId);
        return invoice.map(value -> modelMapper.map(value, InvoiceDTO.class)).orElse(null);
    }

    @Override
    public List<InvoiceDTO> getAllInvoices() {
        List<Invoice> invoices = invoiceRepo.findAll();
        return invoices.stream()
                .map(invoice -> modelMapper.map(invoice, InvoiceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO updateInvoice(int invoiceId, InvoiceDTO invoiceDTO) {
        Optional<Invoice> existingInvoice = invoiceRepo.findById(invoiceId);

        if (existingInvoice.isPresent()) {
            Invoice invoice = existingInvoice.get();
            invoice.setTotalAmount(invoiceDTO.getTotalAmount());
            invoice.setInvoiceDate(LocalDateTime.parse(invoiceDTO.getInvoiceDate()));

            Invoice updatedInvoice = invoiceRepo.save(invoice);
            return modelMapper.map(updatedInvoice, InvoiceDTO.class);
        }

        return null; // Return appropriate error response
    }

    @Override
    public void deleteInvoice(int invoiceId) {
        Optional<Invoice> invoice = invoiceRepo.findById(invoiceId);
        invoice.ifPresent(invoiceRepo::delete);
    }
}
