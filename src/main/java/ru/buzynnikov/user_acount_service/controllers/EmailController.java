package ru.buzynnikov.user_acount_service.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.buzynnikov.user_acount_service.dto.EmailCreateResponse;
import ru.buzynnikov.user_acount_service.dto.EmailRequest;
import ru.buzynnikov.user_acount_service.exceptions.NotAuthenticatedException;
import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;
import ru.buzynnikov.user_acount_service.services.interfasces.EmailService;

/**
 * Контроллер для управления почтовыми адресами пользователей.
 * Реализует методы для добавления, удаления и изменения почтового адреса.
 */
@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Обработчик POST-запроса для добавления нового почтового адреса пользователю.
     *
     * @param request объект запроса, содержащий данные нового e-mail
     * @param authentication объект аутентификации, позволяющий проверить права доступа
     * @return ответ с информацией о создании почты или исключением, если пользователь не авторизован
     */
    @PostMapping
    public ResponseEntity<EmailCreateResponse> addEmail(@Valid @RequestBody EmailRequest request, Authentication authentication) {
        if(authentication.getPrincipal() instanceof UserAuthDTO dto){
            return ResponseEntity.ok(emailService.addEmail(request, dto.getUserId()));
        }
        throw new NotAuthenticatedException("Вы не авторизованы");
    }

    /**
     * Обработчик DELETE-запроса для удаления существующего почтового адреса пользователя.
     *
     * @param request объект запроса, содержащий e-mail для удаления
     * @param authentication объект аутентификации, позволяющий проверить права доступа
     * @return пустой ответ с кодом 204 (No Content), если почта успешно удалена
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteEmail(@RequestBody EmailRequest request, Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserAuthDTO dto) {
            emailService.deleteEmail(request.email(), dto.getUserId());
            return ResponseEntity.noContent().build();
        } else {
            throw new NotAuthenticatedException("Вы не авторизованы");
        }
    }

    /**
     * Обработчик PATCH-запроса для изменения статуса e-mail на "основной".
     *
     * @param request объект запроса, содержащий e-mail для изменения статуса
     * @param authentication объект аутентификации, позволяющий проверить права доступа
     * @return пустой ответ с кодом 204 (No Content), если статус успешно изменён
     */
    @PatchMapping
    public ResponseEntity<Void> setEmailAsDefault(@RequestBody EmailRequest request, Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserAuthDTO dto) {
            emailService.updateEmail(request, dto.getUserId());
            return ResponseEntity.noContent().build();
        } else {
            throw new NotAuthenticatedException("Вы не авторизованы");
        }
    }
}
