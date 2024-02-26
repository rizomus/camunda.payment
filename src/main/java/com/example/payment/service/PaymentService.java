package com.example.payment.service;

import com.example.payment.Entity.Person;
import com.example.payment.Exceptions.PersonNotFoundException;
import com.example.payment.dto.PaymentDto;
import com.example.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    public Long createNewPerson(Person person) {

        Person newPerson = paymentRepository.save(person);
        return newPerson.getId();

    }

    @Transactional
    public PaymentDto makePayment(PaymentDto paymentDto) {
        Person payer = paymentRepository.findById(paymentDto.payerId())
                .orElseThrow(PersonNotFoundException::new);
        return payer.makePayment(paymentDto).getDto();
    }
}
