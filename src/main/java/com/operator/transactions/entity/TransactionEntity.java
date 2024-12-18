package com.operator.transactions.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Сущность, представляющая транзакцию.
 * Хранит информацию о транзакции, такую как сумма, категория, контрагент, дата и пользователь, связанный с транзакцией.
 */
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sum")
    private int sum;

    @Column(name = "category")
    private String category;

    @Column(name = "counterparty")
    private String source;

    @Column(name = "date")
    private LocalDateTime date;

    /**
     * Пользователь, связанный с транзакцией.
     * Связь указывает на {@link UserEntity} и используется для отображения всех транзакций, принадлежащих пользователю.
     * Используется аннотация {@link JsonBackReference} для предотвращения циклической зависимости при JSON-сериализации.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("transactions")
    private UserEntity user;

    public TransactionEntity() {
    }

    public TransactionEntity(Long id, int sum, String category, String source, LocalDateTime date, UserEntity user) {
        this.id = id;
        this.sum = sum;
        this.category = category;
        this.source = source;
        this.date = date;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TransactionEntity{" +
                "id=" + id +
                ", sum=" + sum +
                ", category='" + category + '\'' +
                ", source='" + source + '\'' +
                ", date=" + date +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEntity that = (TransactionEntity) o;
        return sum == that.sum && Objects.equals(id, that.id) && Objects.equals(category, that.category) && Objects.equals(source, that.source) && Objects.equals(date, that.date) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sum, category, source, date, user);
    }
}
