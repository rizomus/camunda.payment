package com.example.payment.Entity;

import com.example.payment.dto.PaymentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long Id;

    private String receiver;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person payer;

    @Transient
    private long payerId;

    private boolean successful;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private CurrencyUnit currencyUnit;

    private LocalDateTime timestamp;

    public Payment(PaymentDto dto) {
        payerId = dto.payerId();
        receiver = dto.receiver();
        amount = dto.amount();
        currencyUnit = CurrencyUnit.valueOf(dto.currencyUnit());
    }

    public PaymentDto getDto() {
        return new PaymentDto(
                amount,
                currencyUnit.name(),
                payerId,
                receiver,
                successful);
    }
}
