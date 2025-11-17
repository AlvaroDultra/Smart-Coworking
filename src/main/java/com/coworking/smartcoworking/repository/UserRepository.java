package com.coworking.smartcoworking.repository;

import com.coworking.smartcoworking.entity.User;
import com.coworking.smartcoworking.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar usuário por email
    Optional<User> findByEmail(String email);

    // Verificar se email já existe
    boolean existsByEmail(String email);

    // Buscar usuários por role
    List<User> findByRole(UserRole role);

    // Buscar usuários ativos
    List<User> findByActiveTrue();

    // Buscar usuários inativos
    List<User> findByActiveFalse();

    // Buscar por nome (contém)
    List<User> findByNameContainingIgnoreCase(String name);
}