package lk.ijse.cellfixbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lk.ijse.cellfixbackend.dto.TechnicianDTO;
import lk.ijse.cellfixbackend.entity.Customer;
import lk.ijse.cellfixbackend.entity.Technician;
import lk.ijse.cellfixbackend.exception.AlreadyExistsException;
import lk.ijse.cellfixbackend.exception.NotFoundException;
import lk.ijse.cellfixbackend.repo.TechnicianRepo;
import lk.ijse.cellfixbackend.service.TechnicianService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        return List.of();
    }
}
