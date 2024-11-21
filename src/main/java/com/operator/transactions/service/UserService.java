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

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
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
        return userMapper.toResponseDTO(getUser(userId));
    }

    public UserResponseDTO updateUser(long userId, UserRequestDTO userRequestDTO) throws UserNotFoundException {
        UserEntity userEntity = getUser(userId);
        userEntity.setName(userRequestDTO.getName());
        userEntity.setSurname(userRequestDTO.getSurname());
        userEntity.setTransactions(userRequestDTO.getTransactions());
        userRepository.saveAndFlush(userEntity);
        return userMapper.toResponseDTO(userEntity);
    }

    public boolean deleteUser(long userId) throws UserNotFoundException {
        if(getUser(userId) == null) {
            return false;
        }
        userRepository.deleteById(userId);
        return true;
    }

    private UserEntity getUser(long userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден!"));
    }

}
