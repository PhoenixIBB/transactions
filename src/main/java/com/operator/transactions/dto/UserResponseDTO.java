package com.operator.transactions.dto;

import com.operator.transactions.entity.TransactionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String name;
    private String surname;
    private List<TransactionEntity> transactions;

}
