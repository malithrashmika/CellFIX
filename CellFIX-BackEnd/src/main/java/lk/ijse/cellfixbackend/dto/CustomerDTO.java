package lk.ijse.cellfixbackend.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor

public class CustomerDTO {
    private String phoneNumber;
    private String name;
    private String email;
    private String address;

    public CustomerDTO(String phoneNumber, String name, String email, String address) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

