package ru.buzynnikov.user_acount_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buzynnikov.user_acount_service.models.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    /**
     * Проверяет существование электронного письма с указанным адресом.
     *
     * @param email адрес электронной почты
     * @return true, если такой email уже зарегистрирован, false в противном случае
     */
    @Transactional
    boolean existsByEmail(String email);

    /**
     * Проверяет существование электронного письма с указанным адресом и привязанным к конкретному пользователю.
     *
     * @param email   адрес электронной почты
     * @param userId  идентификатор пользователя
     * @return true, если такая пара email-userId зарегистрирована, false в противном случае
     */
    @Transactional
    boolean existsByEmailAndUserId(String email, Long userId);

    /**
     * Возвращает количество зарегистрированных электронных писем для указанного пользователя.
     *
     * @param userId идентификатор пользователя
     * @return число электронных писем, принадлежащих данному пользователю
     */
    @Transactional
    @Query("SELECT COUNT(e) FROM Email e WHERE e.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    /**
     * Удаляет электронное письмо по его адресу.
     *
     * @param email адрес электронной почты, подлежащий удалению
     */
    @Transactional
    void deleteByEmail(String email);

    /**
     * Обновляет электронный адрес в базе данных.
     *
     * @param email новый адрес электронной почты
     */
    @Transactional
    @Modifying
    @Query("UPDATE Email e SET e.email = :email WHERE e.email = :email")
    void updateEmail(String email);
}
