package lk.ijse.cellfixbackend.service.impl;

import lk.ijse.cellfixbackend.dto.RepairInventoryDTO;
import lk.ijse.cellfixbackend.entity.RepairInventory;
import lk.ijse.cellfixbackend.entity.RepairJob;
import lk.ijse.cellfixbackend.repo.InventoryRepo;
import lk.ijse.cellfixbackend.repo.RepairInventoryRepo;
import lk.ijse.cellfixbackend.repo.RepairJobRepo;
import lk.ijse.cellfixbackend.service.Invoice_Genarator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class Invoice_GenaratorServiceImpl implements Invoice_Genarator {

    @Autowired
    private RepairJobRepo repairJobRepository;

    @Autowired
    private RepairInventoryRepo repairInventoryRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Override
    public byte[] generateInvoicePdf(int repairJobId) {
        // 1. Get Repair Job
        RepairJob repairJob = repairJobRepository.findById(repairJobId)
                .orElseThrow(() -> new RuntimeException("Repair Job not found"));

        // 2. Get all parts used for that job (directly from repo to avoid circular dependency)
        List<RepairInventory> repairInventories = repairInventoryRepo.findByRepairJobId(repairJobId);
        if (repairInventories.isEmpty()) {
            throw new RuntimeException("No parts found for this repair job.");
        }

        List<RepairInventoryDTO> parts = repairInventories.stream().map(ri -> {
            RepairInventoryDTO dto = new RepairInventoryDTO();
            dto.setPartName(ri.getInventory().getPartName());
            dto.setQuantityUsed(ri.getQuantityUsed());
            dto.setUnitPrice(ri.getInventory().getPrice());
            return dto;
        }).toList();

        // 3. Prepare request to Invoice Generator
        String apiUrl = "https://invoice-generator.com";
        String apiKey = "sk_lGPyg3YtVAwTq9UycCtCGGZVQrfjjn8x"; // Replace if needed

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("from", "CellFIX");
        body.add("to", repairJob.getCustomer().getName());
        body.add("number", "INV-" + repairJobId);
        body.add("date", LocalDate.now().toString());
        body.add("due_date", LocalDate.now().plusDays(7).toString());
        body.add("currency", "LKR");

        // 4. Dynamically add parts
        double totalCost = 0;
        for (int i = 0; i < parts.size(); i++) {
            RepairInventoryDTO part = parts.get(i);
            body.add("items[" + i + "][name]", part.getPartName());
            body.add("items[" + i + "][quantity]", String.valueOf(part.getQuantityUsed()));
            body.add("items[" + i + "][unit_cost]", String.valueOf(part.getUnitPrice()));
            totalCost += part.getQuantityUsed() * part.getUnitPrice();
        }

        body.add("notes", "Thank you for choosing Royal Devices!");
        body.add("terms", "Payment is due on receipt.");
        body.add("discounts", "0");

        // 5. Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(apiKey);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // 6. Send Request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to generate invoice: " + response.getStatusCode());
        }
    }
}
