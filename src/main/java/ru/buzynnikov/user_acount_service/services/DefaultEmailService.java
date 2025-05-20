package ru.buzynnikov.user_acount_service.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.buzynnikov.user_acount_service.aspect.CreateLog;
import ru.buzynnikov.user_acount_service.aspect.UpdateLog;
import ru.buzynnikov.user_acount_service.dto.EmailCreateResponse;
import ru.buzynnikov.user_acount_service.dto.EmailRequest;
import ru.buzynnikov.user_acount_service.exceptions.EmailAlreadyExistException;
import ru.buzynnikov.user_acount_service.exceptions.EmailNotFoundException;
import ru.buzynnikov.user_acount_service.exceptions.LimitEmailException;
import ru.buzynnikov.user_acount_service.models.Email;
import ru.buzynnikov.user_acount_service.models.User;
import ru.buzynnikov.user_acount_service.repositories.EmailRepository;
import ru.buzynnikov.user_acount_service.services.interfasces.EmailService;

/**
 * Реализация сервиса для работы с электронными почтами пользователей
 */
@Service
public class DefaultEmailService implements EmailService {

    private final EmailRepository emailRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public DefaultEmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }


    /**
     * Добавляет новую электронную почту для пользователя
     * @param request DTO с данными для создания почты
     * @param userId идентификатор пользователя
     * @return DTO с информацией о созданной почте
     */
    @CreateLog
    @Transactional
    @Override
    public EmailCreateResponse addEmail(EmailRequest request, Long userId) {
        isEmailExist(request.email());
        return emailResponse(createEmail(request, userId), userId);
    }

    /**
     * Удаляет электронную почту пользователя
     * @param email адрес электронной почты для удаления
     * @param userId идентификатор пользователя
     * @throws EmailNotFoundException если почта не принадлежит пользователю
     * @throws LimitEmailException если у пользователя только одна почта
     */
    @UpdateLog
    @Transactional
    @Override
    public void deleteEmail(String email, Long userId) {
        isEmailOwner(email, userId);
        isEmailGraterThenOne(userId);
        emailRepository.deleteByEmail(email);
    }

    /**
     * Изменяет адрес электронной почты пользователя
     * @param request DTO с данными для изменения почты
     * @param userId идентификатор пользователя
     */
    @UpdateLog
    @Transactional
    @Override
    public void updateEmail(EmailRequest request, Long userId) {
        isEmailOwner(request.email(), userId);
        isEmailExist(request.email());
        emailRepository.updateEmail(request.email());
    }

    /**
     * Создает DTO ответа с информацией о почте
     * @param email объект почты
     * @param userId идентификатор пользователя
     * @return DTO с информацией о почте
     */
    private EmailCreateResponse emailResponse(Email email, Long userId) {
        return new EmailCreateResponse(email.getId(), userId, email.getEmail());
    }

    /**
     * Создает новую запись электронной почты
     * @param request DTO с данными для создания почты
     * @param userId идентификатор пользователя
     * @return созданный объект почты
     * @throws EmailAlreadyExistException если почта уже существует в системе
     */
    private Email createEmail(EmailRequest request, Long userId) {
        isEmailExist(request.email());
        Email email = new Email();
        User userProxy = entityManager.getReference(User.class, userId);
        email.setUser(userProxy);
        email.setEmail(request.email());
        return emailRepository.save(email);
    }

    /**
     * Проверяет существование почты в системе
     * @param email адрес электронной почты для проверки
     * @throws EmailAlreadyExistException если почта уже существует
     */
    private void isEmailExist(String email) {
        if(emailRepository.existsByEmail(email)){
            throw new EmailAlreadyExistException("Email уже существует");
        }
    }

    /**
     * Проверяет принадлежность почты пользователю
     * @param email адрес электронной почты
     * @param userId идентификатор пользователя
     * @throws EmailNotFoundException если почта не принадлежит пользователю
     */
    private void isEmailOwner(String email, Long userId) {
        if (!emailRepository.existsByEmailAndUserId(email, userId)) {
            throw new EmailNotFoundException("Email не найден");
        }
    }

    /**
     * Проверяет что у пользователя больше одной почты
     * @param userId идентификатор пользователя
     * @throws LimitEmailException если у пользователя только одна почта
     */
    private void isEmailGraterThenOne(Long userId) {
        if (emailRepository.countByUserId(userId) < 2) {
            throw new LimitEmailException("У пользователя должен быть хотя бы 1 email");
        }
    }
}
