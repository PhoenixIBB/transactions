package com.operator.transactions.controller;

import com.operator.transactions.dto.TransactionRequestDTO;
import com.operator.transactions.dto.TransactionResponseDTO;
import com.operator.transactions.exception.TransactionNotFoundException;
import com.operator.transactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/{userId}/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(transactionRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions(@PathVariable long userId) {
        return ResponseEntity.ok(transactionService.getTransactionsByUser(userId));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> getTransaction(@PathVariable long transactionId)
            throws TransactionNotFoundException {
        return ResponseEntity.ok(transactionService.getTransaction(transactionId));
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(@PathVariable long transactionId, @RequestBody TransactionRequestDTO transactionRequestDTO) throws TransactionNotFoundException {
        return ResponseEntity.ok(transactionService.updateTransaction(transactionId, transactionRequestDTO));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable long transactionId) throws TransactionNotFoundException {
        boolean isDeleted = transactionService.deleteTransaction(transactionId);
        return isDeleted ?
                ResponseEntity.ok("Zadacha udalena uspeshno.") :
                ResponseEntity.notFound().build();
    }
}
