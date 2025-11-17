package com.coworking.smartcoworking.dto.user;

import com.coworking.smartcoworking.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String name;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Telefone inválido")
    private String phone;

    @NotNull(message = "Role é obrigatório")
    private UserRole role;

    @DecimalMin(value = "0.0", message = "Créditos não pode ser negativo")
    private BigDecimal credits;
}