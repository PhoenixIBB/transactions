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

/**
 * Сервис для работы с транзакциями.
 * Предоставляет методы для создания, получения, обновления и удаления транзакций.
 */
@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Создает новую транзакцию.
     *
     * Этот метод принимает объект запроса для создания новой транзакции, сохраняет его в базу данных
     * и возвращает объект ответа, содержащий информацию о созданной транзакции.
     *
     * @param transactionRequestDTO Объект с данными для создания новой транзакции.
     * @return Ответ с информацией о созданной транзакции в формате DTO.
     */
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO) {

        TransactionEntity transactionEntity = transactionMapper.fromRequestDTO(transactionRequestDTO);
        transactionRepository.saveAndFlush(transactionEntity);

        return transactionMapper.toResponseDTO(transactionEntity);
    }

    /**
     * Получает все транзакции пользователя по его идентификатору.
     *
     * Этот метод принимает идентификатор пользователя и возвращает список всех транзакций, связанных с этим пользователем.
     * Результат возвращается в формате DTO.
     *
     * @param userId Идентификатор пользователя, чьи транзакции нужно получить.
     * @return Список транзакций пользователя в формате DTO.
     */
    public List<TransactionResponseDTO> getTransactionsByUser(long userId) {
        return transactionRepository.findByUserId(userId)
                .stream()
                .map(transactionMapper::toResponseDTO)
                .toList();
    }

    /**
     * Получает транзакцию по ее идентификатору.
     *
     * Этот метод принимает идентификатор транзакции и возвращает информацию о транзакции в формате DTO.
     * В случае, если транзакция не найдена, выбрасывается исключение {@link TransactionNotFoundException}.
     *
     * @param transactionId Идентификатор транзакции, которую нужно получить.
     * @return Ответ с информацией о транзакции в формате DTO.
     * @throws TransactionNotFoundException если транзакция с указанным идентификатором не найдена.
     */
    public TransactionResponseDTO getTransaction(long transactionId) throws TransactionNotFoundException {
        return transactionMapper.toResponseDTO(getTransactionEntity(transactionId));
    }

    /**
     * Обновляет существующую транзакцию.
     *
     * Этот метод принимает идентификатор транзакции и обновленные данные, затем сохраняет изменения в базе данных.
     * Возвращается обновленный объект транзакции в формате DTO.
     * Если транзакция не найдена, выбрасывается исключение {@link TransactionNotFoundException}.
     *
     * @param transactionId Идентификатор транзакции, которую нужно обновить.
     * @param transactionRequestDTO Объект с новыми данными транзакции.
     * @return Ответ с обновленной транзакцией в формате DTO.
     * @throws TransactionNotFoundException если транзакция с указанным идентификатором не найдена.
     */
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

    /**
     * Удаляет транзакцию по ее идентификатору.
     *
     * Этот метод принимает идентификатор транзакции и удаляет ее из базы данных.
     * Возвращает {@code true} если транзакция была удалена.
     * Если транзакция не найдена, выбрасывается исключение {@link TransactionNotFoundException}.
     *
     * @param transactionId Идентификатор транзакции, которую нужно удалить.
     * @return {@code true}, если транзакция успешно удалена.
     * @throws TransactionNotFoundException если транзакция с указанным идентификатором не найдена.
     */
    public boolean deleteTransaction(long transactionId) throws TransactionNotFoundException {
        TransactionEntity transactionEntity = getTransactionEntity(transactionId);
        transactionRepository.delete(transactionEntity);
        return true;
    }

    /**
     * Получает транзакцию по идентификатору.
     *
     * Этот метод использует репозиторий для поиска транзакции в базе данных по ее идентификатору.
     * Если транзакция не найдена, выбрасывается исключение {@link TransactionNotFoundException}.
     *
     * @param transactionId Идентификатор транзакции.
     * @return Транзакция, соответствующая переданному идентификатору.
     * @throws TransactionNotFoundException если транзакция с указанным идентификатором не найдена.
     */
    private TransactionEntity getTransactionEntity(long transactionId) throws TransactionNotFoundException {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Транзакция с Id " +
                        transactionId + " не найдена!"));
    }

}
