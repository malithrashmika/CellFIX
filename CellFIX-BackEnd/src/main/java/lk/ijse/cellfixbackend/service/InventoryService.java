package lk.ijse.cellfixbackend.service;

import lk.ijse.cellfixbackend.dto.CustomerDTO;
import lk.ijse.cellfixbackend.dto.InventoryDTO;
import lk.ijse.cellfixbackend.entity.Customer;
import lk.ijse.cellfixbackend.entity.Inventory;

import java.util.List;

public interface InventoryService {
    Inventory save(InventoryDTO inventoryDTO);
    Inventory update(int id, InventoryDTO inventoryDTO);
    void delete(int id);
    List<InventoryDTO> findAll();
}
