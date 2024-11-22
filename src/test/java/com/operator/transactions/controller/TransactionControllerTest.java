package com.operator.transactions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.operator.transactions.dto.TransactionRequestDTO;
import com.operator.transactions.dto.TransactionResponseDTO;
import com.operator.transactions.entity.UserEntity;
import com.operator.transactions.repository.TransactionRepository;
import com.operator.transactions.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Гарантирует строковой формат

        mockMvc = MockMvcBuilders.standaloneSetup(transactionController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void allTransactions() throws Exception {
        when(transactionService.getTransactionsByUser(1)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/1/transactions", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        verify(transactionService).getTransactionsByUser(1);
    }

    @Test
    void createTransactionTest() throws Exception {
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(1L, 1000
                , "Тестовая транзакция 1", "Контрагент тестовой транзакции 1"
                , LocalDateTime.parse("2024-11-24T15:30:00"), new UserEntity());

        String transactionJson = objectMapper.writeValueAsString(transactionRequestDTO);

        mockMvc.perform(post("/api/1/transactions", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isCreated());
        verify(transactionService).createTransaction(transactionRequestDTO);
    }

    @Test
    void getTransactionByIdTest() throws Exception {
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO(1L, 1000
                , "Тестовая транзакция 2", "Контрагент тестовой транзакции 2"
                , LocalDateTime.parse("2024-11-24T15:30:00"), new UserEntity());

        when(transactionService.getTransaction(1L)).thenReturn(transactionResponseDTO);

        mockMvc.perform(get("/api/1/transactions/{transactionId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sum").value("1000"))
                .andExpect(jsonPath("$.category").value("Тестовая транзакция 2"))
                .andExpect(jsonPath("$.source").value("Контрагент тестовой транзакции 2"))
                .andExpect(jsonPath("$.date").value("2024-11-24T15:30:00"));
    }

    @Test
    void updateTransactionTest() throws Exception {
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(1L, 1000
                , "Обновленная транзакция", "Обновленное контрагент"
                , LocalDateTime.parse("2024-11-24T15:30:00"), new UserEntity());

        String transactionJson = objectMapper.writeValueAsString(transactionRequestDTO);

        TransactionResponseDTO updatedTransaction = new TransactionResponseDTO(1L, 1000
                , "Обновленная транзакция", "Обновленный контрагент"
                , LocalDateTime.parse("2024-11-24T15:30:00"), new UserEntity());
        when(transactionService.updateTransaction(1, transactionRequestDTO)).thenReturn(updatedTransaction);
        mockMvc.perform(put("/api/1/transactions/{transactionId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sum").value("1000"))
                .andExpect(jsonPath("$.category").value("Обновленная транзакция"))
                .andExpect(jsonPath("$.source").value("Обновленный контрагент"))
                .andExpect(jsonPath("$.date").value("2024-11-24T15:30:00"));
    }

    @Test
    void deleteTransactionTest() throws Exception {
        when(transactionService.deleteTransaction(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/1/transactions/{transactionId}", 1L))
                .andExpect(status().isOk());
        when(transactionService.deleteTransaction(1L)).thenReturn(false);
        mockMvc.perform(delete("/api/1/transactions/{transactionId}", 1L))
                .andExpect(status().isNotFound());
    }


}