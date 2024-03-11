package com.example.payment.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Version
    long version;

    @Enumerated(EnumType.STRING)
    private CurrencyUnit currencyUnit;

    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "person_id")
    Person owner;

}
