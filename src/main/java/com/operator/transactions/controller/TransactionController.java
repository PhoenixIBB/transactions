package com.operator.transactions.controller;

import com.operator.transactions.dto.TransactionRequestDTO;
import com.operator.transactions.dto.TransactionResponseDTO;
import com.operator.transactions.exception.TransactionNotFoundException;
import com.operator.transactions.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Класс для приёма запросов на управление транзакциями.
 */
@RestController
@RequestMapping("/api/{userId}/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Создает новую транзакцию для пользователя.
     * <p>
     * Этот метод принимает объект транзакции, созданный клиентом, и сохраняет его в базе данных.
     * Возвращает статус CREATED (201) и сам объект транзакции в формате ResponseDTO.
     *
     * @param transactionRequestDTO Объект запроса, содержащий данные о новой транзакции.
     * @return Ответ с HTTP-статусом 201 и объектом транзакции, включая сгенерированные поля, такие как ID.
     */
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionRequestDTO transactionRequestDTO) {
        logger.info("POST-запрос на /api/{userId}/transactions");
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(transactionRequestDTO));
    }

    /**
     * Получает список всех транзакций пользователя по его идентификатору.
     * <p>
     * Этот метод возвращает все транзакции, связанные с пользователем, в формате списка объектов ResponseDTO.
     * В случае, если у пользователя нет транзакций, возвращается пустой список.
     * Возвращает статус OK (200) и сам список транзакций.
     *
     * @param userId Идентификатор пользователя, для которого нужно получить список транзакций.
     * @return Ответ с HTTP-статусом 200 и списком транзакций, может быть пустым.
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions(@PathVariable long userId) {
        logger.info("GET-запрос на /api/" + userId + "/transactions");
        return ResponseEntity.ok(transactionService.getTransactionsByUser(userId));
    }

    /**
     * Получает информацию о конкретной транзакции по её идентификатору.
     * <p>
     * Этот метод принимает идентификатор транзакции и возвращает её детали в формате DTO.
     * В случае, если транзакция не найдена, выбрасывается исключение TransactionNotFoundException.
     * Возвращает статус OK (200) и объект транзакции, если она найдена.
     *
     * @param transactionId Идентификатор транзакции, которую нужно получить.
     * @return Ответ с HTTP-статусом 200 и объектом транзакции в формате DTO.
     * @throws TransactionNotFoundException если транзакция с данным идентификатором не найдена.
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> getTransaction(@PathVariable long transactionId)
            throws TransactionNotFoundException {
        logger.info("POST-запрос на /api/{userId}/transactions/" + transactionId);
        return ResponseEntity.ok(transactionService.getTransaction(transactionId));
    }

    /**
     * Обновляет данные транзакции.
     * <p>
     * Этот метод принимает идентификатор транзакции и объект с обновленными данными.
     * Если транзакция с данным идентификатором существует, она будет обновлена, и возвращен новый объект транзакции.
     * В случае, если транзакция не найдена, выбрасывается исключение TransactionNotFoundException.
     * Возвращает статус OK (200) и обновленный объект транзакции в формате DTO.
     *
     * @param transactionId         Идентификатор транзакции, которую нужно обновить.
     * @param transactionRequestDTO Объект с новыми данными для обновления транзакции.
     * @return Ответ с HTTP-статусом 200 и обновленным объектом транзакции.
     * @throws TransactionNotFoundException если транзакция с данным идентификатором не найдена.
     */
    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(@PathVariable long transactionId
            , @RequestBody TransactionRequestDTO transactionRequestDTO) throws TransactionNotFoundException {
        logger.info("PUT-запрос на /api/{userId}/transactions/" + transactionId);
        return ResponseEntity.ok(transactionService.updateTransaction(transactionId, transactionRequestDTO));
    }

    /**
     * Удаляет транзакцию по её идентификатору.
     * <p>
     * Этот метод принимает идентификатор транзакции и пытается её удалить. Если транзакция найдена и удалена,
     * возвращается статус OK (200) с сообщением о том, что транзакция успешно удалена.
     * Если транзакция с данным идентификатором не найдена, возвращается статус NOT FOUND (404).
     *
     * @param transactionId Идентификатор транзакции, которую нужно удалить.
     * @return Ответ с HTTP-статусом 200 и сообщением об успешном удалении или статусом 404, если транзакция не найдена.
     * @throws TransactionNotFoundException если транзакция с данным идентификатором не найдена.
     */
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable long transactionId) throws TransactionNotFoundException {
        logger.info("DELETE-запрос на /api/{userId}/transactions" + transactionId);
        boolean isDeleted = transactionService.deleteTransaction(transactionId);
        return isDeleted ?
                ResponseEntity.ok("Zadacha udalena uspeshno.") :
                ResponseEntity.notFound().build();
    }
}
