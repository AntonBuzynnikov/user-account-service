package ru.buzynnikov.user_acount_service.services.interfasces;

import ru.buzynnikov.user_acount_service.dto.CreatePhoneResponse;
import ru.buzynnikov.user_acount_service.dto.PhoneRequest;

public interface PhoneService {
    CreatePhoneResponse addPhone(PhoneRequest request, Long userId);
    void removePhone(String phone, Long userId);

    void updatePhone(String newPhone, Long userId);
}
