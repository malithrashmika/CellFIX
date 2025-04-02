package lk.ijse.cellfixbackend.controller;

import lk.ijse.cellfixbackend.dto.RepairInventoryDTO;
import lk.ijse.cellfixbackend.service.RepairInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/repair-inventory")
public class RepairInventoryController {

    @Autowired
    private RepairInventoryService repairInventoryService;

    @PostMapping("/add")
    public ResponseEntity<RepairInventoryDTO> addRepairInventory(@RequestBody RepairInventoryDTO repairInventoryDTO) {
        RepairInventoryDTO savedRepairInventory = repairInventoryService.addRepairInventory(repairInventoryDTO);
        return ResponseEntity.ok(savedRepairInventory);
    }
}
