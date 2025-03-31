package lk.ijse.cellfixbackend.service;

import lk.ijse.cellfixbackend.dto.TechnicianDTO;
import lk.ijse.cellfixbackend.entity.Technician;

import java.util.List;


public interface TechnicianService {
    Technician save(TechnicianDTO technicianDTO);
    Technician update(int id, TechnicianDTO technicianDTO);
    void delete(int id);
    List<TechnicianDTO> findAll();
}
