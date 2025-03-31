package lk.ijse.cellfixbackend.service;

import lk.ijse.cellfixbackend.dto.InvoiceDTO;

import java.util.List;

public interface InvoiceService {
    InvoiceDTO createInvoice(InvoiceDTO invoiceDTO);

    InvoiceDTO getInvoiceById(int invoiceId);

    List<InvoiceDTO> getAllInvoices();

    InvoiceDTO updateInvoice(int invoiceId, InvoiceDTO invoiceDTO);

    void deleteInvoice(int invoiceId);
}
