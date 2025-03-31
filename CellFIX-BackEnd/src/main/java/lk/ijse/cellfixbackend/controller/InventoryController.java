package lk.ijse.cellfixbackend.controller;


import lk.ijse.cellfixbackend.dto.InventoryDTO;
import lk.ijse.cellfixbackend.dto.TechnicianDTO;
import lk.ijse.cellfixbackend.entity.Inventory;
import lk.ijse.cellfixbackend.entity.Technician;
import lk.ijse.cellfixbackend.exception.AlreadyExistsException;
import lk.ijse.cellfixbackend.exception.NotFoundException;
import lk.ijse.cellfixbackend.service.InventoryService;
import lk.ijse.cellfixbackend.service.TechnicianService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody InventoryDTO inventoryDTO) {
        try {
            Inventory savedInventory =inventoryService.save(inventoryDTO);
            return new ResponseEntity<>(savedInventory, HttpStatus.CREATED);
        }catch (AlreadyExistsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable int id,@RequestBody InventoryDTO inventoryDTO) {
        Inventory updatedInventory=inventoryService.update(id, inventoryDTO);
        return ResponseEntity.ok(updatedInventory);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            inventoryService.delete(id);
            return new ResponseEntity<>("Item deleted", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
