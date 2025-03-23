package lk.ijse.cellfixbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "technicians")
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String expertise;  // e.g., Screen Repair, Battery Replacement

    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL)
    private List<RepairJob> assignedRepairs;
}

