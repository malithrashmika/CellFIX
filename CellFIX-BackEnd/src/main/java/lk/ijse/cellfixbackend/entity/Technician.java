package lk.ijse.cellfixbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "technicians")
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
    private String expertise;  // e.g., Screen Repair, Battery Replacement

    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL)
    private List<RepairJob> repairJobs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public List<RepairJob> getRepairJobs() {
        return repairJobs;
    }

    public void setRepairJobs(List<RepairJob> repairJobs) {
        this.repairJobs = repairJobs;
    }
}

