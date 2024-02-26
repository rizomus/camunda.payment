package com.example.payment.repository;

import com.example.payment.Entity.Payment;
import com.example.payment.Entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Person, Long> {
}
