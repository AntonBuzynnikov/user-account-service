package ru.buzynnikov.user_acount_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PhoneRequest(@NotNull(message = "Телефон не может быть пустым")
                           @Pattern(regexp = "^7\\d{8,12}$", message = "Неверный формат телефона") String phone) {
}
