package ru.buzynnikov.user_acount_service.repositories;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;
import ru.buzynnikov.user_acount_service.models.User;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Репозиторий для работы с моделями пользователей (User).
 * Реализует базовые операции CRUD и специализированные запросы для взаимодействия с базой данных.
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {


    /**
     * Находит пользователя по его адресу электронной почты и формирует объект UserAuthDTO для целей аутентификации.
     *
     * @param email адрес электронной почты пользователя
     * @return объект Optional, содержащий UserAuthDTO с необходимыми полями (ID, email, password),
     * если пользователь найден, или пустой Optional
     */
    @Transactional
    @Query("SELECT new ru.buzynnikov.user_acount_service.security.models.UserAuthDTO(u.id, e.email, u.password) FROM User u JOIN Email e ON u.id = e.user.id WHERE e.email = :email")
    Optional<UserAuthDTO> findByEmailForAuth(String email);

    /**
     * Проверяет существование пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return true, если пользователь с таким идентификатором существует, false в противном случае
     */
    @Transactional
    boolean existsById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.dateOfBirth = :dateOfBirth WHERE u.id = :id")
    void updateDateOfBirth(Long id, LocalDate dateOfBirth);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
    void updateName(Long id, String name);
}
