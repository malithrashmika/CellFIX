package lk.ijse.cellfixbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lk.ijse.cellfixbackend.dto.InventoryDTO;
import lk.ijse.cellfixbackend.entity.Customer;
import lk.ijse.cellfixbackend.entity.Inventory;
import lk.ijse.cellfixbackend.entity.Technician;
import lk.ijse.cellfixbackend.exception.AlreadyExistsException;
import lk.ijse.cellfixbackend.exception.NotFoundException;
import lk.ijse.cellfixbackend.repo.InventoryRepo;
import lk.ijse.cellfixbackend.service.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Inventory save(InventoryDTO inventoryDTO) {
        // Check if technician already exists by id
        if (inventoryRepo.findById(inventoryDTO.getId()).isPresent()) {
            throw new AlreadyExistsException("Technician with ID " + inventoryDTO.getId() + " already exists.");
        } else {
            // Convert DTO to entity
            Inventory inventory = modelMapper.map(inventoryDTO, Inventory.class);

            // Save the technician
            return inventoryRepo.save(inventory);
        }
    }

    @Override
    public Inventory update(int id, InventoryDTO inventoryDTO) {
        Inventory existinginventory = inventoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));

        if (inventoryDTO.getPartName() != null) {
            existinginventory.setPartName(inventoryDTO.getPartName());  // Ensure 'setName()' exists
        }
        if (inventoryDTO.getStockQuantity() != 0) {
            existinginventory.setStockQuantity(inventoryDTO.getStockQuantity());
        }
        if (inventoryDTO.getPrice() != 0) {
            existinginventory.setPrice(inventoryDTO.getPrice());
        }

        return inventoryRepo.save(existinginventory);
    }

    @Override
    public void delete(int id) {
        Inventory inventory = inventoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Item with id " + id + " not found."));

        inventoryRepo.delete(inventory);
    }

    public List<InventoryDTO> findAll() {
        // Fetch inventory items from the repository
        List<Inventory> inventoryList = inventoryRepo.findAll();

        // Use ModelMapper to convert Inventory entities to InventoryDTOs
        return inventoryList.stream()
                .map(inventory -> modelMapper.map(inventory, InventoryDTO.class))
                .collect(Collectors.toList());
    }
}
