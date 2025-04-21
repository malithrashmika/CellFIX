package lk.ijse.cellfixbackend.controller;

import lk.ijse.cellfixbackend.dto.RepairInventoryDTO;
import lk.ijse.cellfixbackend.dto.RepairJobDTO;
import lk.ijse.cellfixbackend.entity.RepairJob;
import lk.ijse.cellfixbackend.service.RepairInventoryService;
import lk.ijse.cellfixbackend.service.RepairJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/repair-inventory")
public class RepairInventoryController {

    @Autowired
    private RepairInventoryService repairInventoryService;

    @Autowired
    private RepairJobService repairJobService;
    @PostMapping("/add")
    public ResponseEntity<RepairInventoryDTO> addRepairInventory(@RequestBody RepairInventoryDTO repairInventoryDTO) {
        RepairInventoryDTO savedRepairInventory = repairInventoryService.addRepairInventory(repairInventoryDTO);
        return ResponseEntity.ok(savedRepairInventory);
    }
    @PutMapping("/complete-with-parts/{id}")
    public ResponseEntity<?> completeRepairWithParts(@PathVariable int id, @RequestBody List<RepairInventoryDTO> partsUsed) {
        try {
            repairInventoryService.completeRepairAndGenerateInvoice(id, partsUsed);
            return ResponseEntity.ok("Repair job completed and invoice generated.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/cancel")
    public ResponseEntity<?> cancelRepairJob(@RequestParam int repairJobId) {
        try {
            repairInventoryService.cancelRepairJob(repairJobId);
            return ResponseEntity.ok().body("Repair job cancelled successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getRepairStatus(@RequestParam String phone) {
        List<RepairJobDTO> repairJobs = repairInventoryService.getRepairStatusByPhone(phone);
        return ResponseEntity.ok(repairJobs);
    }



}
