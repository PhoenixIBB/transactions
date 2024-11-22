package com.operator.transactions.service;

import com.operator.transactions.dto.TransactionRequestDTO;
import com.operator.transactions.dto.TransactionResponseDTO;
import com.operator.transactions.entity.TransactionEntity;
import com.operator.transactions.exception.TransactionNotFoundException;
import com.operator.transactions.repository.TransactionRepository;
import com.operator.transactions.util.TransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Создает новую транзакцию.
     * <p>
     * Этот метод принимает объект запроса для создания новой транзакции, сохраняет его в базу данных
     * и возвращает объект ответа, содержащий информацию о созданной транзакции.
     *
     * @param transactionRequestDTO Объект с данными для создания новой транзакции.
     * @return Ответ с информацией о созданной транзакции в формате DTO.
     */
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO) {
        if (transactionRequestDTO == null) {
            logger.error("Возникла ошибка: получена пустая транзакция.");
            throw new NullPointerException("Получена пустая транзакция");
        }
        logger.debug("Создание транзакции.");
        TransactionEntity transactionEntity = transactionMapper.fromRequestDTO(transactionRequestDTO);
        logger.debug("Сохранение транзакции.");
        transactionRepository.saveAndFlush(transactionEntity);
        logger.debug("Возврат сохраненной транзакции в виде DTO.");
        return transactionMapper.toResponseDTO(transactionEntity);
    }

    /**
     * Получает все транзакции пользователя по его идентификатору.
     * <p>
     * Этот метод принимает идентификатор пользователя и возвращает список всех транзакций, связанных с этим пользователем.
     * Результат возвращается в формате DTO.
     *
     * @param userId Идентификатор пользователя, чьи транзакции нужно получить.
     * @return Список транзакций пользователя в формате DTO.
     */
    public List<TransactionResponseDTO> getTransactionsByUser(long userId) {
        logger.debug("Сбор транзакций пользователя и преобразование их в DTO.");
        return transactionRepository.findByUserId(userId)
                .stream()
                .map(transactionMapper::toResponseDTO)
                .toList();
    }

    /**
     * Получает транзакцию по ее идентификатору.
     * <p>
     * Этот метод принимает идентификатор транзакции и возвращает информацию о транзакции в формате DTO.
     * В случае, если транзакция не найдена, выбрасывается исключение {@link TransactionNotFoundException}.
     *
     * @param transactionId Идентификатор транзакции, которую нужно получить.
     * @return Ответ с информацией о транзакции в формате DTO.
     * @throws TransactionNotFoundException если транзакция с указанным идентификатором не найдена.
     */
    public TransactionResponseDTO getTransaction(long transactionId) throws TransactionNotFoundException {
        logger.debug("Поиск транзакции по ID и возврат DTO.");
        return transactionMapper.toResponseDTO(getTransactionEntity(transactionId));
    }

    /**
     * Обновляет существующую транзакцию.
     * <p>
     * Этот метод принимает идентификатор транзакции и обновленные данные, затем сохраняет изменения в базе данных.
     * Возвращается обновленный объект транзакции в формате DTO.
     * Если транзакция не найдена, выбрасывается исключение {@link TransactionNotFoundException}.
     *
     * @param transactionId         Идентификатор транзакции, которую нужно обновить.
     * @param transactionRequestDTO Объект с новыми данными транзакции.
     * @return Ответ с обновленной транзакцией в формате DTO.
     * @throws TransactionNotFoundException если транзакция с указанным идентификатором не найдена.
     */
    public TransactionResponseDTO updateTransaction(long transactionId, TransactionRequestDTO transactionRequestDTO) throws TransactionNotFoundException {
        logger.debug("Поиск нужной транзакции.");
        TransactionEntity transactionEntity = getTransactionEntity(transactionId);
        logger.debug("Присвоение категории.");
        transactionEntity.setCategory(transactionRequestDTO.getCategory());
        logger.debug("Присвоение даты.");
        transactionEntity.setDate(transactionRequestDTO.getDate());
        logger.debug("Присвоение контрагента.");
        transactionEntity.setSource(transactionRequestDTO.getSource());
        logger.debug("Присвоение суммы.");
        transactionEntity.setSum(transactionRequestDTO.getSum());
        logger.debug("Присвоение пользователя.");
        transactionEntity.setUser(transactionRequestDTO.getUser());
        logger.debug("Сохранение измененной транзакции в базу данных.");
        transactionRepository.saveAndFlush(transactionEntity);
        logger.debug("Возврат измененной транзакции в DTO.");
        return transactionMapper.toResponseDTO(transactionEntity);
    }

    /**
     * Удаляет транзакцию по ее идентификатору.
     * <p>
     * Этот метод принимает идентификатор транзакции и удаляет ее из базы данных.
     * Возвращает {@code true} если транзакция была удалена.
     * Если транзакция не найдена, выбрасывается исключение {@link TransactionNotFoundException}.
     *
     * @param transactionId Идентификатор транзакции, которую нужно удалить.
     * @return {@code true}, если транзакция успешно удалена.
     * @throws TransactionNotFoundException если транзакция с указанным идентификатором не найдена.
     */
    public boolean deleteTransaction(long transactionId) throws TransactionNotFoundException {
        logger.debug("Поиск удаляемой транзакции.");
        TransactionEntity transactionEntity = getTransactionEntity(transactionId);
        logger.debug("Удаление транзакции.");
        transactionRepository.delete(transactionEntity);
        logger.debug("Транзакция удалена.");
        return true;
    }

    /**
     * Получает транзакцию по идентификатору.
     * <p>
     * Этот метод использует репозиторий для поиска транзакции в базе данных по ее идентификатору.
     * Если транзакция не найдена, выбрасывается исключение {@link TransactionNotFoundException}.
     *
     * @param transactionId Идентификатор транзакции.
     * @return Транзакция, соответствующая переданному идентификатору.
     * @throws TransactionNotFoundException если транзакция с указанным идентификатором не найдена.
     */
    private TransactionEntity getTransactionEntity(long transactionId) throws TransactionNotFoundException {
        logger.debug("Поиск транзакции по ID.");
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Транзакция с Id " +
                        transactionId + " не найдена!"));
    }

}
