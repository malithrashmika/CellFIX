package lk.ijse.cellfixbackend.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.ijse.cellfixbackend.dto.RepairJobDTO;
import lk.ijse.cellfixbackend.entity.*;
import lk.ijse.cellfixbackend.repo.*;
import lk.ijse.cellfixbackend.service.RepairJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RepairJobServiceImpl implements RepairJobService {
    @Autowired
    RepairJobRepo repairJobRepo;
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    TechnicianRepo technicianRepo;
    @Autowired
    InvoiceRepo invoiceRepo;
    @Autowired
    InventoryRepo inventoryRepo;
    @Autowired
    RepairInventoryRepo repairInventoryRepo;

    @Override
    public RepairJob assignRepairJob(RepairJobDTO repairJobDTO) throws IOException {
        RepairJob repairJob = new RepairJob();

        repairJob.setDeviceModel(repairJobDTO.getDeviceModel());
        repairJob.setIssueDescription(repairJobDTO.getIssueDescription());
        repairJob.setStatus(repairJobDTO.getStatus());

        // Fetch Customer
        Optional<Customer> customer = customerRepo.findByPhoneNumber(repairJobDTO.getPhoneNumber());
        customer.ifPresent(repairJob::setCustomer);

        // Fetch Technician
        Optional<Technician> technician = technicianRepo.findById(repairJobDTO.getTechnician_id());
        technician.ifPresent(repairJob::setTechnician);
        Optional<Invoice> invoice = invoiceRepo.findById(repairJobDTO.getInvoice_id());
        invoice.ifPresent(repairJob::setInvoice);

        // Convert image
        MultipartFile file = repairJobDTO.getDevicePhoto();
        if (file != null && !file.isEmpty()) {
            repairJob.setDevicePhoto(file.getBytes());
        }

        return repairJobRepo.save(repairJob);
    }

    @Transactional
    @Override
    public RepairJob completeRepairJob(int repairJobId) {
        Optional<RepairJob> repairJobOptional = repairJobRepo.findById(repairJobId);
        if (repairJobOptional.isEmpty()) {
            System.out.println("Repair job ID not found: " + repairJobId);

            throw new RuntimeException("Repair Job not found");
        }

        RepairJob repairJob = repairJobOptional.get();
        if (!"pending".equals(repairJob.getStatus())) {
            throw new RuntimeException("Repair job is already completed or in invalid state");
        }

        Invoice invoice = new Invoice();
        invoice.setRepairJob(repairJob);
        invoice.setInvoiceDate(LocalDateTime.now());

        List<RepairInventory> usedParts = repairInventoryRepo.findByRepairJob(repairJob);
        if (usedParts.isEmpty()) {
            throw new RuntimeException("No parts recorded for this repair job.");
        }

        double totalCost = 0;
        for (RepairInventory repairInventory : usedParts) {
            totalCost += repairInventory.getQuantityUsed() * repairInventory.getInventory().getPrice();
        }

        invoice.setTotalAmount(totalCost);
        invoiceRepo.save(invoice);

        repairJob.setInvoice(invoice);
        repairJob.setStatus("Completed");

        return repairJobRepo.save(repairJob);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void updateRepairJobStatus(int jobId, String status) {
        RepairJob repairJob = repairJobRepo.findById(jobId).orElseThrow(() -> new RuntimeException("Not found"));
        repairJob.setStatus(status);
        entityManager.flush(); // Force Hibernate to push changes to DB
    }
}
