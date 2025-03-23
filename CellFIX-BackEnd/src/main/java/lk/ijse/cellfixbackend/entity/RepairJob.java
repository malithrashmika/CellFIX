package lk.ijse.cellfixbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "repair_jobs")
public class RepairJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String invoiceNumber;  // Unique identifier for repairs

    private String deviceModel;
    private String issueDescription;
    private String status;  // Pending, In Progress, Completed
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_phone", referencedColumnName = "phoneNumber")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @OneToOne(mappedBy = "repairJob", cascade = CascadeType.ALL)
    private Invoice invoice;

    @ManyToMany
    @JoinTable(
            name = "repair_inventory",
            joinColumns = @JoinColumn(name = "repair_id"),
            inverseJoinColumns = @JoinColumn(name = "inventory_id")
    )
    private List<Inventory> usedParts;
}

