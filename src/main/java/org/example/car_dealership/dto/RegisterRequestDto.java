package org.example.car_dealership.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Дто для реєстрації користувача")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {
    @Schema(description = "Ім'я користувача", example = "john_doe")
    @Size(min = 3, max = 30, message = "Ім'я користувача повинно містити від 3 до 30 символів")
    @NotBlank(message = "Ім'я користувача не може бути порожнім")
    private String name;

    @Schema(description = "Електронна пошта користувача", example = "jondoe@mail.com")
    @Size(min = 5, max = 100, message = "Електронна пошта повинна містити від 5 до 100 символів")
    @NotBlank(message = "Електронна пошта не може бути порожньою")
    @Email(message = "Пошта має бути у форматі user@example.com")
    private String email;

    @Schema(description = "Пароль користувача", example = "Password123")
    @Size(max = 100, message = "Пароль повинен містити до 100 символів")
    private String password;
}
