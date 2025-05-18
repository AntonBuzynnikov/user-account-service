package ru.buzynnikov.user_acount_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.buzynnikov.user_acount_service.dto.CreateEmailRequest;
import ru.buzynnikov.user_acount_service.dto.EmailCreateResponse;
import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;
import ru.buzynnikov.user_acount_service.services.interfasces.EmailService;


@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<EmailCreateResponse> addEmail(@RequestBody CreateEmailRequest request, Authentication authentication) {
        if(authentication.getPrincipal() instanceof UserAuthDTO dto){
            return ResponseEntity.ok(emailService.addEmail(request, dto.getUserId()));
        }
        throw new RuntimeException("User is not authenticated");

    }
    @DeleteMapping
    public ResponseEntity<Void> deleteEmail(@RequestParam String email, Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserAuthDTO dto) {
            emailService.deleteEmail(email, dto.getUserId());
            return ResponseEntity.noContent().build();
        } else {
            throw new RuntimeException("User is not authenticated");
        }
    }
}
