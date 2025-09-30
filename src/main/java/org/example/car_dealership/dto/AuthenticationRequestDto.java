package org.example.car_dealership.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель користувача для аутентифікації у вгляді DTO")
public class AuthenticationRequestDto {

    @Schema(description = "Електронна пошта користувача", example = "jondoe@example.com")
    @Size(max = 255, message = "Пошта не може бути довшою за 255 символів")
    @NotBlank(message = "Пошта не може бути порожньою")
    private String email;

    @Schema(description = "Пароль користувача", example = "P@ssw0rd!")
    @Size(max = 255, message = "Пароль має бути до 255 символів")
    @NotBlank(message = "Пароль не може бути порожнім")
    private String password;
}
