package com.imagine.bank.domain;
import javax.persistence.*;
import lombok.Data;
@Entity @Data
public class Investment extends BaseEntity {
    private String symbol;
    private int quantity;
}
