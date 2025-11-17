package com.coworking.smartcoworking.service;

import com.coworking.smartcoworking.dto.user.CreateUserDTO;
import com.coworking.smartcoworking.dto.user.UpdateUserDTO;
import com.coworking.smartcoworking.dto.user.UserResponseDTO;
import com.coworking.smartcoworking.entity.User;
import com.coworking.smartcoworking.exception.BusinessException;
import com.coworking.smartcoworking.exception.ResourceNotFoundException;
import com.coworking.smartcoworking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDTO create(CreateUserDTO dto) {
        // Validar se email já existe
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }

        // Converter DTO → Entity
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // TODO: Criptografar senha
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        user.setCredits(dto.getCredits() != null ? dto.getCredits() : BigDecimal.ZERO);
        user.setActive(true);

        // Salvar no banco
        User saved = userRepository.save(user);

        // Converter Entity → DTO e retornar
        return UserResponseDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));

        return UserResponseDTO.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "email", email));

        return UserResponseDTO.fromEntity(user);
    }

    @Transactional
    public UserResponseDTO update(Long id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));

        // Atualizar apenas os campos que foram enviados (não nulos)
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            // Verificar se o novo email já existe (e não é do próprio usuário)
            if (!dto.getEmail().equals(user.getEmail()) &&
                    userRepository.existsByEmail(dto.getEmail())) {
                throw new BusinessException("Email já cadastrado");
            }
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getActive() != null) {
            user.setActive(dto.getActive());
        }

        User updated = userRepository.save(user);
        return UserResponseDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));

        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findActiveUsers() {
        return userRepository.findByActiveTrue().stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}