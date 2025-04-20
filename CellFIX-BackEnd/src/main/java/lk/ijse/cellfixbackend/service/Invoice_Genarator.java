package lk.ijse.cellfixbackend.service;

import lk.ijse.cellfixbackend.dto.InvoiceDTO;
import lk.ijse.cellfixbackend.dto.RepairInventoryDTO;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface Invoice_Genarator {
    byte[] generateOnlineInvoice(InvoiceDTO invoiceDTO, List<RepairInventoryDTO> partsUsed);
}
