package com.imagine.bank.domain;
import javax.persistence.*;
import lombok.Data;
@Entity @Data
public class Customer extends BaseEntity {
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
}
