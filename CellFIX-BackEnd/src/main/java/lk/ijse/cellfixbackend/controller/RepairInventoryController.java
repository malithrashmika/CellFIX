package lk.ijse.cellfixbackend.controller;

import lk.ijse.cellfixbackend.dto.RepairInventoryDTO;
import lk.ijse.cellfixbackend.entity.RepairJob;
import lk.ijse.cellfixbackend.service.RepairInventoryService;
import lk.ijse.cellfixbackend.service.RepairJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> completeWithParts(
            @PathVariable int id,
            @RequestBody List<RepairInventoryDTO> parts
    ) {
        // maybe validate:
        if (parts.isEmpty()) {
            return ResponseEntity.badRequest().body("No parts provided");
        }

        // Your logic to save repair inventory and generate invoice
        return ResponseEntity.ok("Repair completed with invoice");
    }


}
