package org.example.car_dealership.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Дто для відтповіті з JWT токеном доступу")
public class AuthenticationResponseDto {

    @Schema(description = "Токен доступу", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String token;

    private Long id;

    private String role;

    private String email;

    private String name;
}
