package com.example.payment.dto;

import com.example.payment.Entity.CurrencyUnit;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

public record PaymentDto(

    BigDecimal amount,
    long orderId,
    String currencyUnit,
    long payerId,
    String receiver,
    boolean successful
)
{
}
