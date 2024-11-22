package com.operator.transactions.service;

import com.operator.transactions.dto.TransactionRequestDTO;
import com.operator.transactions.dto.TransactionResponseDTO;
import com.operator.transactions.entity.TransactionEntity;
import com.operator.transactions.exception.TransactionNotFoundException;
import com.operator.transactions.repository.TransactionRepository;
import com.operator.transactions.util.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransactionService {

    private TransactionRepository transactionRepository;
    private TransactionMapper transactionMapper;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO) {

        TransactionEntity transactionEntity = transactionMapper.fromRequestDTO(transactionRequestDTO);
        transactionRepository.saveAndFlush(transactionEntity);

        return transactionMapper.toResponseDTO(transactionEntity);
    }

    public List<TransactionResponseDTO> getTransactionsByUser(long userId) {
        return transactionRepository.findByUserId(userId)
                .stream()
                .map(transactionMapper::toResponseDTO)
                .toList();
    }

    public TransactionResponseDTO getTransaction(long transactionId) throws TransactionNotFoundException {
        return transactionMapper.toResponseDTO(getTransactionEntity(transactionId));
    }

    public TransactionResponseDTO updateTransaction(long transactionId, TransactionRequestDTO transactionRequestDTO) throws TransactionNotFoundException {
        TransactionEntity transactionEntity = getTransactionEntity(transactionId);
        transactionEntity.setCategory(transactionRequestDTO.getCategory());
        transactionEntity.setDate(transactionRequestDTO.getDate());
        transactionEntity.setSource(transactionRequestDTO.getSource());
        transactionEntity.setSum(transactionRequestDTO.getSum());
        transactionEntity.setUser(transactionRequestDTO.getUser());
        transactionRepository.saveAndFlush(transactionEntity);
        return transactionMapper.toResponseDTO(transactionEntity);
    }

    public boolean deleteTransaction(long transactionId) throws TransactionNotFoundException {
        TransactionEntity transactionEntity = getTransactionEntity(transactionId);
        transactionRepository.delete(transactionEntity);
        return true;
    }

    private TransactionEntity getTransactionEntity(long transactionId) throws TransactionNotFoundException {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Транзакция с Id " +
                        transactionId + " не найдена!"));
    }

}
