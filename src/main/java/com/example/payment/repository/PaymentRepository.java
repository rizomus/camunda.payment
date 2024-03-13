package com.example.payment.repository;

import com.example.payment.Entity.Payment;
import com.example.payment.Entity.Person;
import com.example.payment.dto.PaymentCancelDto;
import com.example.payment.dto.PaymentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("""
            SELECT new com.example.payment.dto.PaymentCancelDto(p.amount, p.orderId, p.currencyUnit, p.payer.id, p.receiver) FROM Payment p
            WHERE p.orderId = ?1
            """)
    PaymentCancelDto getOrderInfo(long order_id);


    @Query("""
            SELECT w.id
            FROM Wallet w
            WHERE w.owner.id = (SELECT per.id FROM Person per JOIN Payment pay ON pay.payer.id = per.id WHERE pay.orderId = ?1)
                AND w.currencyUnit = (SELECT currencyUnit FROM Payment WHERE orderId = ?1)
            """)
    long getWalletId(long order_id);


    @Modifying
    @Query("""
            UPDATE Wallet w
            SET w.balance = w.balance + ?2
            WHERE w.id = ?1
            """)
    void updateWalletBalance(long walletId, BigDecimal amount);


    @Modifying
    @Query("""
            UPDATE Payment p
            SET p.payer.id = ?2
            WHERE p.id = ?1
            """)
    void setPayerId(long id, long payerId);
}
