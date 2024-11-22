package com.operator.transactions.controller;

import com.operator.transactions.dto.UserRequestDTO;
import com.operator.transactions.dto.UserResponseDTO;
import com.operator.transactions.exception.UserNotFoundException;
import com.operator.transactions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для работы с пользователями.
 * Предоставляет методы для создания, получения и удаления пользователей.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Создает нового пользователя.
     *
     * Этот метод принимает объект с данными пользователя и создает нового пользователя в системе.
     * Если создание пользователя прошло успешно, возвращается статус OK (200) с объектом нового пользователя в формате DTO.
     *
     * @param userRequestDTO Объект с данными для создания нового пользователя.
     * @return Ответ с HTTP-статусом 200 и созданным пользователем в формате DTO.
     * @throws UserNotFoundException если пользователь с таким ID не найден (не актуально для данного метода, но можно использовать для обработки других исключений).
     */
    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) throws UserNotFoundException {
        return ResponseEntity.ok(userService.createUser(userRequestDTO));
    }

    /**
     * Получает список всех пользователей.
     *
     * Этот метод возвращает список всех пользователей, зарегистрированных в системе.
     * Если в системе нет пользователей, возвращается пустой список. Ответ содержит статус OK (200) и список пользователей в формате DTO.
     *
     * @return Ответ с HTTP-статусом 200 и списком всех пользователей в формате DTO.
     * @throws UserNotFoundException если пользователей в системе нет (не обязательно выбрасывать исключение, можно вернуть пустой список).
     */
    @GetMapping("/")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() throws UserNotFoundException {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Получает информацию о пользователе по его идентификатору.
     *
     * Этот метод принимает идентификатор пользователя и возвращает его данные.
     * Если пользователь не найден, возвращается статус NOT FOUND (404).
     *
     * @param userId Идентификатор пользователя, чьи данные нужно получить.
     * @return Ответ с HTTP-статусом 200 и объектом пользователя в формате DTO, если пользователь найден.
     *         В случае отсутствия пользователя с данным ID возвращается статус 404.
     * @throws UserNotFoundException если пользователь с данным ID не найден.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable long userId) throws UserNotFoundException {
        UserResponseDTO userEntity = userService.getUserById(userId);
        return userEntity != null ?
                ResponseEntity.ok(userEntity) :
                ResponseEntity.notFound().build();
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * Этот метод принимает идентификатор пользователя и удаляет его из системы.
     * В случае успешного удаления возвращается статус NO CONTENT (204).
     * Если пользователь не найден, возвращается статус NOT FOUND (404).
     *
     * @param userId Идентификатор пользователя, которого необходимо удалить.
     * @return Ответ с HTTP-статусом 204 (NO CONTENT), если пользователь был успешно удален, или 404 (NOT FOUND), если пользователь не найден.
     * @throws UserNotFoundException если пользователь с данным ID не найден.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long userId) throws UserNotFoundException {
        return userService.deleteUser(userId) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }


}
