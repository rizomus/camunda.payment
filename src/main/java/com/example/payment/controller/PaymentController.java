package com.example.payment.controller;

import com.example.payment.Entity.*;
import com.example.payment.Exceptions.PersonNotFoundException;
import com.example.payment.dto.PaymentDto;
import com.example.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;


    private void addPerson(String name) {
        ArrayList<Wallet> wallets = new ArrayList<>();

        wallets.add(
                Wallet.builder()
                        .balance(BigDecimal.valueOf(Math.random() * 10000))
                        .currencyUnit(CurrencyUnit.RUB)
                        .build()
        );

        wallets.add(
                Wallet.builder()
                        .balance(BigDecimal.valueOf(Math.random() * 10000))
                        .currencyUnit(CurrencyUnit.EUR)
                        .build()
        );
        wallets.add(
                Wallet.builder()
                        .balance(BigDecimal.valueOf(Math.random() * 10000))
                        .currencyUnit(CurrencyUnit.USD)
                        .build()
        );

        Person person = Person.builder()
                .name(name)
                .wallets(wallets)
                .build();

        BigDecimal amount = BigDecimal.valueOf(Math.random() * 10000);
        System.out.println("\n  BALANCE: " + wallets.get(0).getBalance());
        System.out.println("        Try to make payment of " + amount + "\n");

        Payment payment = person.makePayment(amount, CurrencyUnit.RUB, "Bank", 3210L);

        System.out.println("Returned payment amount: " + payment.getAmount() + "\n");

        Long id = paymentService.createNewPerson(person);
    }


    @PostMapping("/test-content-load")
    public ResponseEntity<String> testContentLoad() {

        addPerson("Vasya");
        addPerson("Petya");
        addPerson("Masha");

        return new ResponseEntity<>("Content created", HttpStatus.CREATED);
    }


    @PostMapping("pay")
    private ResponseEntity<PaymentDto> pay(@RequestBody PaymentDto paymentRequest) {

        PaymentDto paymentDto = null;

        try {
            paymentDto = paymentService.makePayment(paymentRequest);
        } catch (PersonNotFoundException e) {
            return new ResponseEntity<>(paymentRequest, HttpStatus.NOT_FOUND);
        }

        if (paymentDto.successful()) {
            return new ResponseEntity<>(paymentDto, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(paymentDto, HttpStatus.BAD_REQUEST);
        }

    }
}
