package lk.ijse.cellfixbackend.controller;

import lk.ijse.cellfixbackend.service.Invoice_Genarator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoicesController {

    @Autowired
    private Invoice_Genarator invoiceGenarator;

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadInvoice(@RequestParam int repairJobId) {
        byte[] pdfBytes = invoiceGenarator.generateInvoicePdf(repairJobId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("invoice.pdf").build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

}
