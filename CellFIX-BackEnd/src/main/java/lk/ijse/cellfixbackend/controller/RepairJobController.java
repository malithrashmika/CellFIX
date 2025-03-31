package lk.ijse.cellfixbackend.controller;

import lk.ijse.cellfixbackend.dto.RepairJobDTO;
import lk.ijse.cellfixbackend.service.RepairJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestBody RepairJobDTO repairJobDTO
    ) {
        try {
            repairJobService.assignRepairJob(repairJobDTO);
            return ResponseEntity.ok("Repair job assigned successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    @PostMapping(value = "/assign", consumes = { "multipart/form-data" })
    public ResponseEntity<String> assignRepairJob(
            @RequestPart("repairJobDTO") RepairJobDTO repairJobDTO,
            @RequestPart(value = "devicePhoto", required = false) MultipartFile devicePhoto
    ) {
        try {
            repairJobDTO.setDevicePhoto(devicePhoto);
            repairJobService.assignRepairJob(repairJobDTO);
            return ResponseEntity.ok("Repair job assigned successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }




}
