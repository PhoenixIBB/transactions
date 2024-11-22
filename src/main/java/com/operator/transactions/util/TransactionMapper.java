package com.operator.transactions.util;

import com.operator.transactions.dto.TransactionRequestDTO;
import com.operator.transactions.dto.TransactionResponseDTO;
import com.operator.transactions.dto.UserRequestDTO;
import com.operator.transactions.dto.UserResponseDTO;
import com.operator.transactions.entity.TransactionEntity;
import com.operator.transactions.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionRequestDTO toRequestDTO(TransactionEntity transactionEntity);

    TransactionResponseDTO toResponseDTO(TransactionEntity transactionEntity);

    TransactionEntity fromRequestDTO(TransactionRequestDTO transactionRequestDTO);

    TransactionEntity fromResponseDTO(TransactionResponseDTO transactionResponseDTO);

}
