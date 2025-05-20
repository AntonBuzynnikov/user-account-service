package ru.buzynnikov.user_acount_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountTransferRequest(Long toId, @NotNull(message = "Сумма перевода должна быть указана.")
                                                @Min(value = 10, message = "Сумма перевода должна быть не менее 10 рублей.") BigDecimal amount) {
}
