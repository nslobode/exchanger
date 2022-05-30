package org.exchange.exchanger.services;

import lombok.RequiredArgsConstructor;
import org.exchange.exchanger.dto.UserDto;
import org.exchange.exchanger.entities.Account;
import org.exchange.exchanger.entities.Conversion;
import org.exchange.exchanger.entities.User;
import org.exchange.exchanger.exceptions.BrokeException;
import org.exchange.exchanger.exceptions.RecordNotFoundException;
import org.exchange.exchanger.repo.ConversionRepo;
import org.exchange.exchanger.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final UserRepository userRepository;
    private final ConversionRepo conversionRepo;
    private final static String PAIR = "eurgbp";
    private double conversionRate;
    @Value("${conversion.margin}")
    private double margin;

    private static final int PRECISION = 2;

    private void init() {
        if (conversionRate != 0) { return;}
        Conversion conversion = conversionRepo.findById(PAIR)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Record with id %s not found", PAIR)));
        conversionRate = conversion.getRate();
    }

    public String spend(UserDto userDto, String currency, BigDecimal amount) {
        amount = amount.setScale(PRECISION, HALF_UP);
        User user = userRepository.findByPassport(userDto.getPassport());
        BigDecimal reminder;
        switch (currency) {
            case "gbp":
                Account accountGbp = user.getAccountGbp();
                reminder = withdraw(amount, accountGbp);
                userRepository.save(user);
                break;
            case "eur":
                Account accountEur = user.getAccountEur();
                reminder = withdraw(amount, accountEur);
                userRepository.save(user);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currency);
        }
        return reminder.toString();
    }

    public String earn(UserDto userDto, String currency, BigDecimal amount) {
        amount = amount.setScale(PRECISION, HALF_UP);
        User user = userRepository.findByPassport(userDto.getPassport());
        BigDecimal reminder;
        switch (currency) {
            case "gbp":
                Account accountGbp = user.getAccountGbp();
                reminder = insert(amount, accountGbp);
                userRepository.save(user);
                break;
            case "eur":
                Account accountEur = user.getAccountEur();
                reminder = insert(amount, accountEur);
                userRepository.save(user);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currency);
        }
        return reminder.toString();
    }

    public String sell(UserDto userDto, String operationAccount, BigDecimal amount) {
        init();
        User user = userRepository.findByPassport(userDto.getPassport());
        BigDecimal reminder;
        Account accountEur = user.getAccountEur();
        Account accountGbp = user.getAccountGbp();
        switch (operationAccount) {
            case "eur":
                reminder = sellEur(accountEur, accountGbp, amount);
                userRepository.save(user);
                break;
            case "gbp":
                reminder = sellGbp(accountGbp, accountEur, amount);
                userRepository.save(user);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + operationAccount);
        }
        return reminder.toString();
    }

    public String buy(UserDto userDto, String operationAccount, BigDecimal amount) {
        init();
        User user = userRepository.findByPassport(userDto.getPassport());
        BigDecimal reminder;
        Account accountEur = user.getAccountEur();
        Account accountGbp = user.getAccountGbp();
        switch (operationAccount) {
            case "eur":
                reminder = buyEur(accountEur, accountGbp, amount);
                userRepository.save(user);
                break;
            case "gbp":
                reminder = buyGbp(accountGbp, accountEur, amount);
                userRepository.save(user);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + operationAccount);
        }
        return reminder.toString();
    }

    public String checkConversion(String currencyToConvert, BigDecimal amount) {
        init();
        BigDecimal result;
        switch (currencyToConvert) {
            case "eur":
                result = amount.multiply(valueOf(conversionRate))
                        .multiply(valueOf(1 - margin / 100)).setScale(PRECISION, HALF_UP);
                break;
            case "gbp":
                result = amount.multiply(valueOf(1/conversionRate))
                        .multiply(valueOf(1 - margin / 100)).setScale(PRECISION, HALF_UP);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currencyToConvert);
        }
        return result.toPlainString();
    }

    private BigDecimal buyEur(Account accountEur, Account accountGbp, BigDecimal amount) {
        BigDecimal currentFrom = accountGbp.getBalance();
        checkWithdrawal(amount, currentFrom);
        withdraw(amount, accountGbp);
        BigDecimal result = amount.multiply(valueOf(1/conversionRate))
                .multiply(valueOf(1 - margin / 100)).setScale(PRECISION, HALF_UP);
        insert(result, accountEur);
        return accountGbp.getBalance();
    }

    private BigDecimal buyGbp(Account accountGbp, Account accountEur, BigDecimal amount) {
        BigDecimal currentFrom = accountEur.getBalance();
        checkWithdrawal(amount, currentFrom);
        withdraw(amount, accountEur);
        BigDecimal result = amount.multiply(valueOf(conversionRate))
                .multiply(valueOf(1 - margin / 100)).setScale(PRECISION, HALF_UP);
        insert(result, accountGbp);
        return accountEur.getBalance();
    }

    private BigDecimal sellEur(Account from, Account to, BigDecimal amount) {
        BigDecimal currentFrom = from.getBalance();
        checkWithdrawal(amount, currentFrom);
        withdraw(amount, from);
        BigDecimal result = amount.multiply(valueOf(conversionRate))
                .multiply(valueOf(1 - margin / 100)).setScale(PRECISION, HALF_UP);
        insert(result, to);
        return from.getBalance();
    }

    private BigDecimal sellGbp(Account from, Account to, BigDecimal amount) {
        BigDecimal currentFrom = from.getBalance();
        checkWithdrawal(amount, currentFrom);
        withdraw(amount, from);
        BigDecimal result = amount.multiply(valueOf(1/conversionRate))
                .multiply(valueOf(1 - margin / 100).setScale(PRECISION, HALF_UP));
        insert(result, to);
        return from.getBalance();
    }

    private void checkWithdrawal(BigDecimal amount, BigDecimal currentFrom) {
        if (currentFrom.compareTo(amount) < 0) {
            throw new BrokeException("ha ha");
        }
    }

    private BigDecimal withdraw(BigDecimal amount, Account account) {
        BigDecimal current = account.getBalance();
        checkWithdrawal(amount, current);
        BigDecimal reminder = current.setScale(PRECISION, HALF_UP).subtract(amount);
        account.setBalance(reminder);
        return reminder;
    }

    private BigDecimal insert(BigDecimal amount, Account account) {
        BigDecimal current = account.getBalance();
        BigDecimal reminder = current.setScale(PRECISION, HALF_UP).add(amount);
        account.setBalance(reminder);
        return reminder;
    }
}
