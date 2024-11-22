package com.operator.transactions.dto;

import com.operator.transactions.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public class TransactionRequestDTO {

    private Long id;
    private int sum;
    private String category;
    private String source;
    private LocalDateTime date;
    private UserEntity user;

    public TransactionRequestDTO() {
    }

    public TransactionRequestDTO(Long id, int sum, String category, String source, LocalDateTime date, UserEntity user) {
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
        return "TransactionRequestDTO{" +
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
        TransactionRequestDTO that = (TransactionRequestDTO) o;
        return sum == that.sum && Objects.equals(id, that.id) && Objects.equals(category, that.category) && Objects.equals(source, that.source) && Objects.equals(date, that.date) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sum, category, source, date, user);
    }
}
