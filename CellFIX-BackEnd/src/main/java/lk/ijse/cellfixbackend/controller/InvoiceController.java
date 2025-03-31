package lk.ijse.cellfixbackend.controller;

import lk.ijse.cellfixbackend.dto.InvoiceDTO;
import lk.ijse.cellfixbackend.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // Create a new invoice
    @PostMapping("/save")
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO createdInvoice = invoiceService.createInvoice(invoiceDTO);
        return createdInvoice != null
                ? ResponseEntity.ok(createdInvoice)
                : ResponseEntity.badRequest().build();
    }

    // Get invoice by ID
    @GetMapping("/update/{invoiceId}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable int invoiceId) {
        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(invoiceId);
        return invoiceDTO != null
                ? ResponseEntity.ok(invoiceDTO)
                : ResponseEntity.notFound().build();
    }

    // Get all invoices
    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        List<InvoiceDTO> invoiceDTOs = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoiceDTOs);
    }

    // Update an invoice
    @PutMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDTO> updateInvoice(@PathVariable int invoiceId, @RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO updatedInvoice = invoiceService.updateInvoice(invoiceId, invoiceDTO);
        return updatedInvoice != null
                ? ResponseEntity.ok(updatedInvoice)
                : ResponseEntity.notFound().build();
    }

    // Delete an invoice
    @DeleteMapping("/delete/{invoiceId}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable int invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
        return ResponseEntity.noContent().build();
    }
}
