package lk.ijse.cellfixbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lk.ijse.cellfixbackend.dto.TechnicianDTO;
import lk.ijse.cellfixbackend.entity.Technician;
import lk.ijse.cellfixbackend.exception.AlreadyExistsException;
import lk.ijse.cellfixbackend.exception.NotFoundException;
import lk.ijse.cellfixbackend.repo.TechnicianRepo;
import lk.ijse.cellfixbackend.service.TechnicianService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TechnicianServiceImpl implements TechnicianService {
    @Autowired
    private TechnicianRepo technicianRepo;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Technician save(TechnicianDTO technicianDTO) {
        // Check if technician already exists by id
        if (technicianRepo.findById(technicianDTO.getId()).isPresent()) {
            throw new AlreadyExistsException("Technician with ID " + technicianDTO.getId() + " already exists.");
        } else {
            // Convert DTO to entity
            Technician technician = modelMapper.map(technicianDTO, Technician.class);

            // Save the technician
            return technicianRepo.save(technician);
        }
    }


    @Override
    public Technician update(int id, TechnicianDTO technicianDTO) {
        Technician existingTechnician = technicianRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Technician not found with id: " + id));

        if (technicianDTO.getName() != null) {
            existingTechnician.setName(technicianDTO.getName());  // Ensure 'setName()' exists
        }
        if (technicianDTO.getEmail() != null) {
            existingTechnician.setEmail(technicianDTO.getEmail());
        }
        if (technicianDTO.getExpertise() != null) {
            existingTechnician.setExpertise(technicianDTO.getExpertise());
        }

        return technicianRepo.save(existingTechnician);
    }
    @Override
    public void delete(int id) {
        Technician technician = technicianRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with phone number " + id + " not found."));

       technicianRepo.delete(technician);

    }
    @Override
    public List<TechnicianDTO> findAll() {
        List<Technician> technicians = technicianRepo.findAll();

        return technicians.stream()
                .map(technician-> modelMapper.map( technician, TechnicianDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public List<Integer> getAllids() {
        return technicianRepo.findAll()
                .stream()
                .map(Technician::getId)
                .collect(Collectors.toList());
    }
}
