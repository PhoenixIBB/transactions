package com.operator.transactions.utils;

import com.operator.transactions.dto.UserRequestDTO;
import com.operator.transactions.dto.UserResponseDTO;
import com.operator.transactions.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserRequestDTO toRequestDTO(UserEntity userEntity);

    UserResponseDTO toResponseDTO(UserEntity userEntity);

    UserEntity fromRequestDTO(UserRequestDTO userRequestDTO);

    UserEntity fromResponseDTO(UserResponseDTO userResponseDTO);

}
