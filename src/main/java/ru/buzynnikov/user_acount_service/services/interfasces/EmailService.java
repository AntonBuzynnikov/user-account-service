package ru.buzynnikov.user_acount_service.services.interfasces;


import ru.buzynnikov.user_acount_service.dto.CreateEmailRequest;
import ru.buzynnikov.user_acount_service.dto.EmailCreateResponse;

public interface EmailService {
    EmailCreateResponse addEmail(CreateEmailRequest request, Long userId);
    void deleteEmail(String email, Long userId);
}
