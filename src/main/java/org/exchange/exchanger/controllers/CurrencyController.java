package org.exchange.exchanger.controllers;

import lombok.RequiredArgsConstructor;
import org.exchange.exchanger.dto.UserDto;
import org.exchange.exchanger.services.CurrencyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @PostMapping(value = "/currency/spend", produces = "application/x-yaml")
    public String spend(UserDto userDto, String currency, BigDecimal amount) {
        return currencyService.spend(userDto, currency, amount);
    }

    @PostMapping(value = "/currency/earn", produces = "application/x-yaml")
    public String earn(UserDto userDto, String currency, BigDecimal amount) {
        return currencyService.earn(userDto, currency, amount);
    }

//    @PostMapping(value = "/currency/sell", produces = "application/x-yaml")
    public String sell(UserDto userDto, String account, BigDecimal amount) {
        return currencyService.sell(userDto, account, amount);
    }

//    @PostMapping(value = "/currency/buy", produces = "application/x-yaml")
    public String buy(UserDto userDto, String account, BigDecimal amount) {
        return currencyService.buy(userDto, account, amount);
    }

    @PostMapping(value = "/currency/convert", produces = "application/x-yaml")
    public String checkConversion(String currency, BigDecimal amount) {
        return currencyService.checkConversion(currency, amount);
    }
}
