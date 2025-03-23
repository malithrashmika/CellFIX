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
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double totalAmount;
    private LocalDateTime invoiceDate;

    @OneToOne
    @JoinColumn(name = "repair_id")
    private RepairJob repairJob;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<Payment> payments;
}

