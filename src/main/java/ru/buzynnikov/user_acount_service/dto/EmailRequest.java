package ru.buzynnikov.user_acount_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record EmailRequest(@NotNull(message = "Email не может быть пустым")
                           @Pattern(regexp = "([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)", message = "Некорректный формат email") String email) {
}
