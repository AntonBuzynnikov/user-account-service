package ru.buzynnikov.user_acount_service.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.buzynnikov.user_acount_service.dto.CreatePhoneResponse;
import ru.buzynnikov.user_acount_service.dto.PhoneRequest;
import ru.buzynnikov.user_acount_service.exceptions.NotAuthenticatedException;
import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;
import ru.buzynnikov.user_acount_service.services.interfasces.PhoneService;

/**
 * Контроллер для управления телефонными номерами пользователей.
 * Включает методы для добавления и удаления номеров телефонов.
 */
@RestController
@RequestMapping("/api/v1/phone")
public class PhoneController {

    private final PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    /**
     * Обработчик POST-запроса для добавления нового телефонного номера пользователю.
     *
     * @param request объект запроса, содержащий телефонный номер
     * @param authentication объект аутентификации, позволяющий проверять права доступа
     * @return ответ с информацией о добавленном номере или исключение, если пользователь не авторизован
     */
    @PostMapping
    public ResponseEntity<CreatePhoneResponse> createPhone(@Valid @RequestBody PhoneRequest request, Authentication authentication){
        if(authentication.getPrincipal() instanceof UserAuthDTO dto){
            return ResponseEntity.ok(phoneService.addPhone(request, dto.getUserId()));
        }
        throw new NotAuthenticatedException("Вы не авторизованы");
    }

    @PatchMapping
    public ResponseEntity<Void> updatePhone(@Valid @RequestBody PhoneRequest request, Authentication authentication){
        if(authentication.getPrincipal() instanceof UserAuthDTO dto) {
            phoneService.updatePhone(request.phone(), dto.getUserId());
            return ResponseEntity.noContent().build();
        } else {
            throw new NotAuthenticatedException("Вы не авторизованы");
        }
    }

    /**
     * Обработчик DELETE-запроса для удаления телефонного номера пользователя.
     *
     * @param request объект запроса, содержащий номер телефона для удаления
     * @param authentication объект аутентификации, позволяющий проверять права доступа
     * @return пустой ответ с кодом 204 (No Content), если телефон успешно удалён
     */
    @DeleteMapping
    public ResponseEntity<Void> deletePhone(@Valid @RequestBody PhoneRequest request, Authentication authentication){
        if(authentication.getPrincipal() instanceof UserAuthDTO dto){
            phoneService.removePhone(request.phone(), dto.getUserId());
        }
        throw new NotAuthenticatedException("Вы не авторизованы");
    }
}
