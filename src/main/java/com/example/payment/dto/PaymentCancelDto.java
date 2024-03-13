package com.example.payment.dto;

import com.example.payment.Entity.CurrencyUnit;

import java.math.BigDecimal;

public record PaymentCancelDto(

    BigDecimal amount,
    long orderId,
    CurrencyUnit currencyUnit,
    long payerId,
    String receiver
)
{
}
