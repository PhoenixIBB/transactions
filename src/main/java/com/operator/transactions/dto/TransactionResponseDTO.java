package com.operator.transactions.dto;

import com.operator.transactions.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {

    private Long id;
    private int sum;
    private String category;
    private String source;
    private LocalDateTime date;
    private User user;

}
