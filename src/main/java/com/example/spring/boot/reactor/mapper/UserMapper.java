package com.example.spring.boot.reactor.mapper;

import com.example.spring.boot.reactor.dto.UserDTO;
import com.example.spring.boot.reactor.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.control.DeepClone;

@Mapper(mappingControl = DeepClone.class, componentModel = "spring")
public interface UserMapper {

    User mapToUser(UserDTO userDTO);
}
