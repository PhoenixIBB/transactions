package com.operator.transactions.service;

import com.operator.transactions.dto.UserRequestDTO;
import com.operator.transactions.dto.UserResponseDTO;
import com.operator.transactions.entity.UserEntity;
import com.operator.transactions.exception.UserNotFoundException;
import com.operator.transactions.repository.UserRepository;
import com.operator.transactions.util.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с пользователями.
 * Предоставляет методы для создания, получения, обновления и удаления пользователей.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Создает нового пользователя.
     * <p>
     * Этот метод принимает объект запроса для создания нового пользователя, сохраняет его в базу данных
     * и возвращает объект ответа с информацией о созданном пользователе.
     *
     * @param userRequestDTO Объект с данными для создания нового пользователя.
     * @return Ответ с информацией о созданном пользователе в формате DTO.
     */
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        logger.debug("Преобразование UserRequestDTO в UserEntity.");
        UserEntity userEntity = userMapper.fromRequestDTO(userRequestDTO);
        logger.debug("Сохранение UserEntity в базу данных.");
        userRepository.saveAndFlush(userEntity);
        logger.debug("Преобразование UserEntity в UserResponseDTO и возврат объекта.");
        return userMapper.toResponseDTO(userEntity);
    }

    /**
     * Получает всех пользователей.
     * <p>
     * Этот метод возвращает список всех пользователей. Если пользователи не найдены, выбрасывается
     * исключение {@link UserNotFoundException}.
     *
     * @return Список пользователей в формате DTO.
     * @throws UserNotFoundException если пользователи не найдены.
     */
    public List<UserResponseDTO> getAllUsers() throws UserNotFoundException {
        logger.debug("Поиск всех пользователей в базе данных.");
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()) {
            logger.error("Возникла ошибка: пользователи не найдены.");
            throw new UserNotFoundException("Пользователи не найдены.");
        }
        logger.debug("Преобразование списка UserEntity в список UserResponseDTO и его возврат.");
        return users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получает пользователя по его идентификатору.
     * <p>
     * Этот метод принимает идентификатор пользователя и возвращает информацию о нем в формате DTO.
     * В случае, если пользователь не найден, выбрасывается исключение {@link UserNotFoundException}.
     *
     * @param userId Идентификатор пользователя, которого нужно получить.
     * @return Ответ с информацией о пользователе в формате DTO.
     * @throws UserNotFoundException если пользователь с указанным идентификатором не найден.
     */
    public UserResponseDTO getUserById(long userId) throws UserNotFoundException {
        logger.debug("Поиск пользователя по ID и преобразование его в DTO.");
        return userMapper.toResponseDTO(getUser(userId));
    }

    /**
     * Обновляет данные пользователя.
     * <p>
     * Этот метод принимает идентификатор пользователя и обновленные данные, затем сохраняет изменения в базе данных.
     * Возвращается обновленный объект пользователя в формате DTO.
     * Если пользователь не найден, выбрасывается исключение {@link UserNotFoundException}.
     *
     * @param userId         Идентификатор пользователя, которого нужно обновить.
     * @param userRequestDTO Объект с новыми данными пользователя.
     * @return Ответ с обновленным пользователем в формате DTO.
     * @throws UserNotFoundException если пользователь с указанным идентификатором не найден.
     */
    public UserResponseDTO updateUser(long userId, UserRequestDTO userRequestDTO) throws UserNotFoundException {
        logger.debug("Поиск пользователя по ID.");
        UserEntity userEntity = getUser(userId);
        logger.debug("Присвоение имени.");
        userEntity.setName(userRequestDTO.getName());
        logger.debug("Присвоение фамилии.");
        userEntity.setSurname(userRequestDTO.getSurname());
        logger.debug("Присвоение списка транзакций.");
        userEntity.setTransactions(userRequestDTO.getTransactions());
        logger.debug("Сохранение пользователя в список.");
        userRepository.saveAndFlush(userEntity);
        logger.debug("Преобразование пользователя в DTO и возврат.");
        return userMapper.toResponseDTO(userEntity);
    }

    /**
     * Удаляет пользователя.
     * <p>
     * Этот метод принимает идентификатор пользователя и удаляет его из базы данных.
     * Возвращает {@code true} если пользователь был удален.
     * Если пользователь не найден, возвращается {@code false}.
     *
     * @param userId Идентификатор пользователя, которого нужно удалить.
     * @return {@code true}, если пользователь был успешно удален, {@code false} если не найден.
     * @throws UserNotFoundException если пользователь с указанным идентификатором не найден.
     */
    public boolean deleteUser(long userId) throws UserNotFoundException {
        logger.debug("Попытка удаления пользователя.");
        if (getUser(userId) == null) {
            logger.error("Пользователь не найден.");
            return false;
        }
        logger.debug("Удаление пользователя.");
        userRepository.deleteById(userId);
        logger.debug("Пользователь удалён.");
        return true;
    }

    /**
     * Получает пользователя по идентификатору.
     * <p>
     * Этот метод выполняет поиск пользователя по его идентификатору в базе данных. Если пользователь не найден,
     * выбрасывается исключение {@link UserNotFoundException}.
     *
     * @param userId Идентификатор пользователя.
     * @return Пользователь, соответствующий переданному идентификатору.
     * @throws UserNotFoundException если пользователь с указанным идентификатором не найден.
     */
    private UserEntity getUser(long userId) throws UserNotFoundException {
        logger.debug("Получение пользователя по ID.");
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден!"));
    }

}
