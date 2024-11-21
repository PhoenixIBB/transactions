package com.operator.transactions.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 5)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Поле 'имя' не может быть пустым")
    @Size(max = 55, message = "Длина имени не должна превышать 55 символов")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Поле 'фамилия' не может быть пустым")
    @Size(max = 55, message = "Длина фамилии не должна превышать 55 символов")
    @Column(name = "surname")
    private String surname;

    @OneToMany(mappedBy = "user"
            , cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    private List<TransactionEntity> transactions;

}
