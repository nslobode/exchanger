package org.exchange.exchanger.mappers;

import org.exchange.exchanger.dto.UserDto;
import org.exchange.exchanger.entities.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    User fromDto(UserDto dto);
    UserDto fromEntity(User entity);
}
