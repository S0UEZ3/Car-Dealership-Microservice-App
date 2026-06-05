package org.example.application.services;

import lombok.RequiredArgsConstructor;
import org.example.application.exceptions.DomainValidationException;
import org.example.application.exceptions.EntityNotFoundException;
import org.example.dataAccess.models.Role;
import org.example.dataAccess.models.User;
import org.example.dataAccess.repositories.IUserRepository;
import org.example.presentation.dto.mapper.UserMapper;
import org.example.presentation.dto.objects.UserDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final IUserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto findById(UUID userId) {
        return userMapper.toDto(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId)));
    }

    public List<UserDto> findAll() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    public List<UserDto> findByRole(Role role) {
        return userMapper.toDtoList(userRepository.findAll().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList()));
    }

    public List<UserDto> findClients() {
        return userMapper.toDtoList(userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.CLIENT)
                .collect(Collectors.toList()));
    }

    @Transactional
    public UserDto save(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        validateNewUser(user);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public UserDto update(UserDto userDto) {
        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userDto.getId()));
        validateUserUpdate(existingUser, existingUser);
        userMapper.updateEntityFromDto(userDto, existingUser);
        validateNewUser(existingUser);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    public void deleteById(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found: " + userId);
        }
        userRepository.deleteById(userId);
    }

    private void validateNewUser(User user) {
        if (user == null) {
            throw new DomainValidationException("User cannot be null");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new DomainValidationException("Email is required");
        }

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new DomainValidationException("Name is required");
        }

        if (isEmailExists(user.getEmail())) {
            throw new DomainValidationException("Email already exists");
        }
    }

    private void validateUserUpdate(User updatedUser, User existingUser) {
        if (updatedUser == null) {
            throw new DomainValidationException("User data cannot be null");
        }

        if (updatedUser.getEmail() != null && updatedUser.getEmail().trim().isEmpty()) {
            throw new DomainValidationException("Email cannot be empty");
        }

        if (updatedUser.getEmail() != null &&
                !updatedUser.getEmail().equals(existingUser.getEmail()) &&
                isEmailExists(updatedUser.getEmail())) {
            throw new DomainValidationException("Email already exists");
        }

        if (updatedUser.getName() != null && updatedUser.getName().trim().isEmpty()) {
            throw new DomainValidationException("Name cannot be empty");
        }
    }

    private boolean isEmailExists(String email) {
        return userRepository.findAll().stream()
                .anyMatch(user -> email.equalsIgnoreCase(user.getEmail()));
    }
}
