package lk.ijse.cellfixbackend.service.impl;

import lk.ijse.cellfixbackend.dto.RepairJobDTO;
import lk.ijse.cellfixbackend.entity.*;
import lk.ijse.cellfixbackend.repo.*;
import lk.ijse.cellfixbackend.service.RepairJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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

    @Override
    public RepairJob completeRepairJob(int repairJobId) {
        Optional<RepairJob> repairJobOptional = repairJobRepo.findById(repairJobId);
        if (repairJobOptional.isPresent()) {
            RepairJob repairJob = repairJobOptional.get();

            if ("Completed".equals(repairJob.getStatus())) {
                Invoice invoice = new Invoice();
                invoice.setRepairJob(repairJob);
                invoice.setInvoiceDate(LocalDateTime.now());

                // Calculate total cost from used parts
                double totalCost = 0;
                for (RepairInventory repairInventory : repairJob.getRepairInventories()) {
                    totalCost += repairInventory.getQuantityUsed() * repairInventory.getInventory().getPrice();
                }
                invoice.setTotalAmount(totalCost);


                // Save the invoice
                invoiceRepo.save(invoice);
                repairJob.setInvoice(invoice); // Link the invoice to repair job

                return repairJobRepo.save(repairJob);
            }
        }
        return null;
    }


}
