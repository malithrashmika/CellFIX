package lk.ijse.cellfixbackend.controller;


import lk.ijse.cellfixbackend.dto.CustomerDTO;
import lk.ijse.cellfixbackend.dto.TechnicianDTO;
import lk.ijse.cellfixbackend.entity.Technician;
import lk.ijse.cellfixbackend.exception.AlreadyExistsException;
import lk.ijse.cellfixbackend.exception.NotFoundException;
import lk.ijse.cellfixbackend.service.TechnicianService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/technician")
public class TechnicianController {
    @Autowired
    private TechnicianService technicianService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody TechnicianDTO technicianDTO) {
        try {
            Technician savedTechnician=technicianService.save(technicianDTO);
            return new ResponseEntity<>(savedTechnician, HttpStatus.CREATED);
        }catch (AlreadyExistsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable int id,@RequestBody TechnicianDTO technicianDTO) {
        Technician updatedTechnician=technicianService.update(id, technicianDTO);
        return ResponseEntity.ok(updatedTechnician);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            technicianService.delete(id);
            return new ResponseEntity<>("Technician deleted", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<TechnicianDTO>> getAllTechnicians() {
        List<TechnicianDTO> technicians = technicianService.findAll();
        return new ResponseEntity<>(technicians, HttpStatus.OK);
    }
}
