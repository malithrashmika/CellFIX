package lk.ijse.cellfixbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "repair_jobs")
public class RepairJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String deviceModel;
    private String issueDescription;
    @Lob
    @Column(name = "device_photo", columnDefinition = "LONGBLOB")
    private byte[] devicePhoto;
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

    @OneToMany(mappedBy = "repairJob", cascade = CascadeType.ALL)
    private List<RepairInventory> repairInventories;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public byte[] getDevicePhoto() {
        return devicePhoto;
    }

    public void setDevicePhoto(byte[] devicePhoto) {
        this.devicePhoto = devicePhoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public List<RepairInventory> getRepairInventories() {
        return repairInventories;
    }

    public void setRepairInventories(List<RepairInventory> repairInventories) {
        this.repairInventories = repairInventories;
    }
}

