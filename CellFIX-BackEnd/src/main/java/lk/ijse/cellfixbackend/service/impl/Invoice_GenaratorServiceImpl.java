package lk.ijse.cellfixbackend.service.impl;

import com.codingerror.model.AddressDetails;
import com.codingerror.model.HeaderDetails;
import com.codingerror.model.ProductTableHeader;
import com.codingerror.service.CodingErrorPdfInvoiceCreator;
import lk.ijse.cellfixbackend.dto.InvoiceDTO;
import lk.ijse.cellfixbackend.dto.RepairInventoryDTO;
import lk.ijse.cellfixbackend.service.Invoice_Genarator;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class Invoice_GenaratorServiceImpl implements Invoice_Genarator {
    public byte[] generateOnlineInvoice(InvoiceDTO invoiceDTO, List<RepairInventoryDTO> partsUsed) {
        RestTemplate restTemplate = new RestTemplate();

        String apiUrl = "https://invoice-generator.com";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer YOUR_API_KEY"); // Replace with your real key

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("from", "CellFIX");
        body.add("to", invoiceDTO.getCustomerName());
        body.add("number", String.valueOf(invoiceDTO.getInvoice_id()));
        body.add("date", invoiceDTO.getInvoiceDate());
        body.add("due_date", invoiceDTO.getInvoiceDate());
        body.add("notes", "Thank you for choosing CellFIX!");
        body.add("terms", "Payment is due on receipt.");

        for (int i = 0; i < partsUsed.size(); i++) {
            RepairInventoryDTO part = partsUsed.get(i);
            body.add("items[" + i + "][name]", "Part ID: " + part.getInventoryId());
            body.add("items[" + i + "][quantity]", String.valueOf(part.getQuantityUsed()));
            body.add("items[" + i + "][unit_cost]", "100"); // TODO: use real price later
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<byte[]> response = restTemplate.postForEntity(apiUrl, request, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // This is the PDF
        } else {
            throw new RuntimeException("Failed to generate invoice: " + response.getStatusCode());
        }
    }



}
