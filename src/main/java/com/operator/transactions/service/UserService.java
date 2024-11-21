package com.operator.transactions.service;

import com.operator.transactions.entity.UserEntity;
import com.operator.transactions.exceptions.UserNotFoundException;
import com.operator.transactions.repository.UserRepository;
import com.operator.transactions.utils.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.operator.transactions.dto.UserResponseDTO;
import com.operator.transactions.dto.UserRequestDTO;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO createUser(long id, UserRequestDTO userRequestDTO) throws UserNotFoundException {
        if (userRepository.findById(id).isPresent()) {
            throw new UserNotFoundException("Пользователь не найден!");
        }
        UserEntity userEntity = userMapper.fromRequestDTO(userRequestDTO);
        userRepository.saveAndFlush(userEntity);
        return userMapper.toResponseDTO(userEntity);
    }

    public List<UserResponseDTO> getAllUsers() throws UserNotFoundException {
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("Пользователи не найдены.");
        }
        return users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(long userId) throws UserNotFoundException {
        return getUser(userId);
    }

    public UserResponseDTO updateUserProperty(long id, Consumer<UserEntity> updater) throws UserNotFoundException {
        UserEntity userForUpdating = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден."));
        updater.accept(userForUpdating);
        return userMapper.toResponseDTO(userRepository.saveAndFlush(userForUpdating));
    }

    public boolean deleteUser(long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    private UserResponseDTO getUser(long userId) throws UserNotFoundException {
        return userMapper.toResponseDTO(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден!")));
    }

}
