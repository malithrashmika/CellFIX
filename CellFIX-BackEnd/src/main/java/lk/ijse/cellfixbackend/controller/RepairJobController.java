package lk.ijse.cellfixbackend.controller;

import lk.ijse.cellfixbackend.dto.RepairJobDTO;
import lk.ijse.cellfixbackend.dto.RepairJobResponseDTO;
import lk.ijse.cellfixbackend.entity.RepairJob;
import lk.ijse.cellfixbackend.service.RepairJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/repairjob")
public class RepairJobController {
    @Autowired
    private final RepairJobService repairJobService;

    public RepairJobController(RepairJobService repairJobService) {
        this.repairJobService = repairJobService;
    }

    @PostMapping("/assign")
    public ResponseEntity<String> assignRepairJob(
//            @RequestBody RepairJobDTO repairJobDTO
            @RequestPart("deviceModel") String deviceModel,
            @RequestPart("issueDescription") String issueDescription,
            @RequestPart("status") String status,
            @RequestPart("phoneNumber") String phoneNumber,
            @RequestPart("technician_id") String technician_id,
            @RequestPart("devicePhoto") MultipartFile devicePhoto
    ) {

//        System.out.println(repairJobDTO.getDevicePhoto());
        RepairJobDTO repairJobDTO = new RepairJobDTO();

        repairJobDTO.setDeviceModel(deviceModel);
        repairJobDTO.setIssueDescription(issueDescription);
        repairJobDTO.setStatus(status);
        repairJobDTO.setPhoneNumber(phoneNumber);
        repairJobDTO.setTechnician_id(Integer.parseInt(technician_id));
        repairJobDTO.setDevicePhoto(devicePhoto);

        try {
            repairJobService.assignRepairJob(repairJobDTO);
            return ResponseEntity.ok("Repair job assigned successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

//    @PostMapping(value = "/assign", consumes = {"multipart/form-data"})
//    public ResponseEntity<String> assignRepairJob(
//            @RequestPart("repairJobDTO") RepairJobDTO repairJobDTO,
//            @RequestPart(value = "devicePhoto", required = false) MultipartFile devicePhoto
//    ) {
//        try {
//            repairJobDTO.setDevicePhoto(devicePhoto);
//            repairJobService.assignRepairJob(repairJobDTO);
//            return ResponseEntity.ok("Repair job assigned successfully!");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }

    @PutMapping("/complete/{id}")
    public ResponseEntity<?> completeRepairJob(@PathVariable int id) {
        System.out.println("Received request to complete repair job with ID: " + id);

        RepairJob updatedJob = repairJobService.completeRepairJob(id);

        if (updatedJob == null) {
            System.out.println("Repair job not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Repair job not found");
        }


        System.out.println("Repair job completed successfully!");
        return ResponseEntity.ok(updatedJob);
    }

    @GetMapping
    public ResponseEntity<List<RepairJobResponseDTO>> getAllRepairJobs() {
        return ResponseEntity.ok(repairJobService.getAllRepairJobs());
    }
}
