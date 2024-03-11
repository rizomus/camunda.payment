package com.example.payment.Entity;

import com.example.payment.dto.PaymentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private List<Wallet> wallets = new ArrayList<>();

    public Payment makePayment(BigDecimal amount, CurrencyUnit paymentCurrency, String receiver, long orderId) {

        boolean result = false;

        for (Wallet wallet :
                this.wallets) {
            if (wallet.getCurrencyUnit() == paymentCurrency && wallet.getBalance().compareTo(amount) >= 0) {
                wallet.setBalance(wallet.getBalance().subtract(amount));
                result = true;
                break;
            }
        }

        var payment = Payment.builder()
                .amount(amount)
                .orderId(orderId)
                .currencyUnit(paymentCurrency)
                .timestamp(LocalDateTime.now())
                .successful(result)
                .payerId(this.getId())
                .receiver(receiver)
                .build();

        if (this.payments == null) {
            this.payments = new ArrayList<>();
        }
        this.payments.add(payment);

        return payment;
    }

    public Payment makePayment(PaymentDto paymentDto) {
        BigDecimal amount = paymentDto.amount();
        CurrencyUnit paymentCurrency = CurrencyUnit.valueOf(paymentDto.currencyUnit());
        String receiver = paymentDto.receiver();
        long orderId = paymentDto.orderId();

        return makePayment(amount, paymentCurrency, receiver, orderId);
    }
}
