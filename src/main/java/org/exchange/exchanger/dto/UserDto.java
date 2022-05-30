package org.exchange.exchanger.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDto {

    private String name;
    private Integer passport;
    private BigDecimal eur;
    private BigDecimal usd;
}