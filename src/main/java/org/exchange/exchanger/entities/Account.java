package org.exchange.exchanger.entities;

import java.math.BigDecimal;

public interface Account {

    BigDecimal getBalance();
    void setBalance(BigDecimal balance);
}
