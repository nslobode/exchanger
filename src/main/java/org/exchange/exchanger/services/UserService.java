package org.exchange.exchanger.services;

import lombok.RequiredArgsConstructor;
import org.exchange.exchanger.dto.UserDto;
import org.exchange.exchanger.entities.AccountEur;
import org.exchange.exchanger.entities.AccountGbp;
import org.exchange.exchanger.entities.User;
import org.exchange.exchanger.mappers.UserMapper;
import org.exchange.exchanger.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User getUserByPassport(Integer passport) {
        return userRepository.findByPassport(passport);
    }

    public UserDto saveUser(UserDto userDto) {
        User userEntity = userMapper.fromDto(userDto);
        User byPassport = userRepository.findByPassport(userDto.getPassport());
        User entityToReturn;
        if (byPassport != null) {
            entityToReturn = byPassport;
        } else {
            User persistent = userRepository.save(userEntity);
            AccountGbp accountGbp = new AccountGbp();
            accountGbp.setBalance(BigDecimal.ZERO);
            AccountEur accountEur = new AccountEur();
            accountEur.setBalance(BigDecimal.ZERO);
            persistent.setAccountEur(accountEur);
            accountEur.setUser(persistent);
            accountGbp.setUser(persistent);
            persistent.setAccountGbp(accountGbp);
            entityToReturn = userRepository.save(persistent);
        }

        return userMapper.fromEntity(entityToReturn);
    }


}
