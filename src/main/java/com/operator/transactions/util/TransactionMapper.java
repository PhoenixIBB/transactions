package com.operator.transactions.util;

import com.operator.transactions.dto.TransactionRequestDTO;
import com.operator.transactions.dto.TransactionResponseDTO;
import com.operator.transactions.entity.TransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionResponseDTO toResponseDTO(TransactionEntity transactionEntity);

    TransactionEntity fromRequestDTO(TransactionRequestDTO transactionRequestDTO);

}
