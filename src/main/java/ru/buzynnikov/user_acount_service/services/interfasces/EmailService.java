package ru.buzynnikov.user_acount_service.services.interfasces;


import ru.buzynnikov.user_acount_service.dto.EmailCreateResponse;
import ru.buzynnikov.user_acount_service.dto.EmailRequest;

public interface EmailService {
    EmailCreateResponse addEmail(EmailRequest request, Long userId);
    void deleteEmail(String email, Long userId);
    void updateEmail(EmailRequest request, Long userId);
}
