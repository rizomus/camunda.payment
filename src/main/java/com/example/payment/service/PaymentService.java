package com.example.payment.service;

import com.example.payment.Entity.Payment;
import com.example.payment.Entity.Person;
import com.example.payment.Exceptions.PersonNotFoundException;
import com.example.payment.dto.PaymentCancelDto;
import com.example.payment.dto.PaymentDto;
import com.example.payment.repository.PaymentRepository;
import com.example.payment.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PersonRepository personRepository;

    public Long createNewPerson(Person person) {

        Person newPerson = personRepository.save(person);
        return newPerson.getId();

    }

    @Transactional
    public PaymentDto makePayment(PaymentDto paymentDto) {
        Person payer = personRepository.findById(paymentDto.payerId())
                .orElseThrow(PersonNotFoundException::new);
        return payer.makePayment(paymentDto).getDto();
    }

    @Transactional
    public void cancelPayment(long orderId) {

        PaymentCancelDto orderInfo = paymentRepository.getOrderInfo(orderId);
        log.debug(orderInfo.toString());

        long walletId = paymentRepository.getWalletId(orderId);
        log.debug("wallet: " + walletId);

        paymentRepository.updateWalletBalance(walletId, orderInfo.amount());

        Payment negatePayment = Payment.builder()
                .orderId(orderId)
                .payerId(orderInfo.payerId())
                .currencyUnit(orderInfo.currencyUnit())
                .amount(orderInfo.amount().negate())
                .timestamp(LocalDateTime.now())
                .receiver(orderInfo.receiver())
                .successful(true)
                .build();

        Payment payment = paymentRepository.save(negatePayment);
        paymentRepository.setPayerId(payment.getId(), orderInfo.payerId());

    }
}
