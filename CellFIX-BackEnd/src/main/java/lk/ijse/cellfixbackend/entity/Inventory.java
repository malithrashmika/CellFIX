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
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String partName;
    private int stockQuantity;
    private double price;

    @ManyToMany(mappedBy = "usedParts")
    private List<RepairJob> repairsUsedIn;
}

