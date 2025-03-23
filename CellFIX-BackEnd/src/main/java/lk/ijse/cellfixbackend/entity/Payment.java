package lk.ijse.cellfixbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amountPaid;
    private LocalDateTime paymentDate;
    private String paymentMethod; // Cash, Card, Online

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}

