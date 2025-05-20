package ru.buzynnikov.user_acount_service.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.buzynnikov.user_acount_service.aspect.CreateLog;
import ru.buzynnikov.user_acount_service.aspect.UpdateLog;
import ru.buzynnikov.user_acount_service.dto.CreatePhoneResponse;
import ru.buzynnikov.user_acount_service.dto.PhoneRequest;
import ru.buzynnikov.user_acount_service.exceptions.LimitPhoneException;
import ru.buzynnikov.user_acount_service.exceptions.PhoneAlreadyExistsException;
import ru.buzynnikov.user_acount_service.exceptions.PhoneNotFoundException;
import ru.buzynnikov.user_acount_service.models.Phone;
import ru.buzynnikov.user_acount_service.models.User;
import ru.buzynnikov.user_acount_service.repositories.PhoneRepository;
import ru.buzynnikov.user_acount_service.services.interfasces.PhoneService;

/**
 * Реализация сервиса для работы с телефонными номерами.
 * Включает методы для добавления, удаления и обновления номеров.
 */

@Service
public class PhoneServiceDefault implements PhoneService {

    private final PhoneRepository phoneRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public PhoneServiceDefault(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    /**
     * Добавляет новый телефонный номер пользователю.
     *
     * @param request объект запроса с данными о телефоне
     * @param userId  идентификатор пользователя
     * @return объект CreatePhoneResponse с информацией о добавленном номере
     */
    @CreateLog
    @Transactional
    @Override
    public CreatePhoneResponse addPhone(PhoneRequest request, Long userId) {
        isPhoneExist(request.phone());
        return createPhoneResponse(createPhone(request, userId), userId);
    }

    /**
     * Удаляет телефонный номер пользователя.
     *
     * @param phone  номер телефона
     * @param userId идентификатор пользователя
     */
    @UpdateLog
    @Transactional
    @Override
    public void removePhone(String phone, Long userId) {
        isPhoneOwner(phone, userId);
        isPhoneGraterThenOne(userId);
        phoneRepository.deleteByPhone(phone);
    }

    /**
     * Обновляет телефонный номер пользователя.
     *
     * @param newPhone новый номер телефона
     * @param userId   идентификатор пользователя
     */
    @UpdateLog
    @Transactional
    @Override
    public void updatePhone(String newPhone, Long userId) {
        isPhoneOwner(newPhone, userId);
        isPhoneExist(newPhone);
        phoneRepository.updatePhone(newPhone);
    }


    /**
     * Формирует объект CreatePhoneResponse с информацией о добавленном номере.
     *
     * @param phone   созданный объект Phone
     * @param userId  идентификатор пользователя
     * @return объект CreatePhoneResponse
     */
    private CreatePhoneResponse createPhoneResponse(Phone phone, Long userId) {
        return new CreatePhoneResponse(phone.getId(), userId, phone.getPhone());
    }

    /**
     * Создает новый телефонный номер для пользователя.
     *
     * @param request объект запроса с данными о телефоне
     * @param userId  идентификатор пользователя
     * @return созданный объект Phone
     */
    private Phone createPhone(PhoneRequest request, Long userId) {
        isPhoneExist(request.phone());
        User userProxy = entityManager.getReference(User.class, userId);
        return phoneRepository.save(new Phone(userProxy, request.phone()));
    }

    /**
     * Проверяет доступность номера телефона.
     *
     * @param phone номер телефона
     * @throws PhoneAlreadyExistsException если номер уже занят
     */
    private void isPhoneExist(String phone) {
        if(phoneRepository.existsByPhone(phone))
            throw new PhoneAlreadyExistsException(String.format("Номер телефона %s уже существует", phone));
    }

    /**
     * Проверяет, является ли пользователь владельцем указанного номера телефона.
     *
     * @param phone  номер телефона
     * @param userId идентификатор пользователя
     * @throws PhoneNotFoundException если пользователь не владеет указанным номером
     */
    private void isPhoneOwner(String phone, Long userId) {
        if (!phoneRepository.existsByPhoneAndUserId(phone, userId))
                throw new PhoneNotFoundException(String.format("Пользователь с id %d не владелец номера телефона %s", userId, phone));

    }

    /**
     * Проверяет, что у пользователя больше одного номера телефона.
     *
     * @param userId идентификатор пользователя
     * @throws LimitPhoneException если у пользователя останется менее одного номера
     */
    private void isPhoneGraterThenOne(Long userId){
        if(phoneRepository.countByUserId(userId) < 2)
            throw new LimitPhoneException(String.format("У пользователя %d не может быть меньше одного номера телефона", userId));
    }
}
