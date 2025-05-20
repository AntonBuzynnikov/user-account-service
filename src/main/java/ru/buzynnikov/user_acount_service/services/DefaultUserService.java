package ru.buzynnikov.user_acount_service.services;


import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.buzynnikov.user_acount_service.aspect.UpdateLog;
import ru.buzynnikov.user_acount_service.dto.UserResponse;
import ru.buzynnikov.user_acount_service.models.User;
import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;

import ru.buzynnikov.user_acount_service.exceptions.UserNotFoundException;
import ru.buzynnikov.user_acount_service.repositories.UserRepository;
import ru.buzynnikov.user_acount_service.services.interfasces.UserService;
import ru.buzynnikov.user_acount_service.utils.UserSpecification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Реализация сервиса для работы с пользователями.
 * Включает методы для поиска пользователей, проверки их существования и получения деталей профиля.
 */
@CacheConfig(cacheNames = "users")
@Service
public class DefaultUserService implements UserService {


    private final UserRepository userRepository;


    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Возвращает информацию о пользователе по его email.
     * Используется в процессе аутентификации пользователя.
     * @param email адрес электронной почты пользователя
     * @return объект UserAuthDTO с данными пользователя
     * @throws UserNotFoundException если пользователь с указанным email не найден
     */
    @Override
    public UserAuthDTO getUserByEmail(String email) {
        return userRepository.findByEmailForAuth(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь с email: %s не найден", email)));
    }

    /**
     * Проверяет существование пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @throws UserNotFoundException если пользователь с указанным идентификатором не найден
     */
    @Transactional
    @Override
    public void isUserExists(Long id) {
        if (!userRepository.existsById(id))
            throw new UserNotFoundException(String.format("Пользователь с id: %d не найден", id));
    }

    /**
     * Выполняет поиск пользователей по спецификации и страничный вывод.
     *
     * @param specification условия поиска (JPQL Specifications)
     * @param pageable      параметры страницы (размер страницы, порядок сортировки и т.д.)
     * @return страница объектов UserResponse с результатами поиска
     */
    @Override
    @Cacheable(key = "{#email, #dateOfBirth, #phone, #name, #pageable.pageNumber, #pageable.pageSize, #pageable.sort?.toString()}" )
    public Page<UserResponse> findUser(String email, String name, String phone, String dateOfBirth, Pageable pageable) {
        Specification<User> specification = Specification
                .where(UserSpecification.hasEmail(email))
                .and(UserSpecification.byNameLike(name))
                .and(UserSpecification.byDateOfBirth(dateOfBirth))
                .and(UserSpecification.hasPhone(phone));
        return userRepository.findAll(specification, pageable)
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
    }
    @UpdateLog
    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @Override
    public void updateDateOfBird(Long userId, String dateOfBirth) {
        userRepository.updateDateOfBirth(userId, parseDateOfBirth(dateOfBirth));
    }
    @UpdateLog
    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @Override
    public void updateName(Long userId, String name) {
        userRepository.updateName(userId, name);
    }

    private LocalDate parseDateOfBirth(String dateOfBirth) {
        return LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }


}
