package ru.buzynnikov.user_acount_service.controllers;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.buzynnikov.user_acount_service.dto.AccountTransferRequest;
import ru.buzynnikov.user_acount_service.exceptions.NotAuthenticatedException;
import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;
import ru.buzynnikov.user_acount_service.services.interfasces.AccountService;

import java.math.BigDecimal;

/**
 * Контроллер, ответственный за управление аккаунтами пользователей.
 * Предоставляет API для осуществления переводов между счетами.
 */
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Осуществляет перевод средств между счетами пользователей.
     *
     * @param request объект запроса, содержащий ID аккаунта назначения и сумму перевода
     * @param authentication объект аутентификации, хранящий данные авторизованного пользователя
     * @return пустой ответ с кодом 204 (No Content), если перевод успешно выполнен
     * @throws NotAuthenticatedException если пользователь не авторизован
     */
    @PutMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody AccountTransferRequest request, Authentication authentication) {
        if(authentication.getPrincipal() instanceof UserAuthDTO dto){
            accountService.transferTo(dto.getUserId(), request.toId(), request.amount());
            return ResponseEntity.noContent().build();
        }
        throw new NotAuthenticatedException("Вы не авторизованы");
    }
    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(Authentication authentication) {
        if(authentication.getPrincipal() instanceof UserAuthDTO dto){
            return ResponseEntity.ok(accountService.getBalance(dto.getUserId()));
        }
        throw new NotAuthenticatedException("Вы не авторизованы");
    }
}
