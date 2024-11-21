package com.operator.transactions.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactios")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sum")
    private int sum;

    @NotBlank(message = "Поле 'Категория' не должно быть пустым.")
    @Column(name = "category")
    private String category;

    @NotBlank(message = "Поле 'Контрагент' не должно быть пустым.")
    @Column(name = "counterparty")
    private String source;

    @NotEmpty(message = "Поле 'Дата' не должно быть пустым.")
    @Column(name = "date")
    private LocalDateTime date;

    @NotNull(message = "Поле 'Пользователь' не должно быть пустым.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
