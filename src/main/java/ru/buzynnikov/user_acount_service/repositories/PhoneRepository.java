package ru.buzynnikov.user_acount_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buzynnikov.user_acount_service.models.Phone;

/**
 * Репозиторий для работы с моделями телефонных номеров (Phone).
 * Поддерживает базовые операции CRUD и специализированные запросы для взаимодействия с базой данных.
 */
@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    /**
     * Подсчитывает количество телефонных номеров, прикрепленных к указанному пользователю.
     *
     * @param userId идентификатор пользователя
     * @return количество телефонных номеров пользователя
     */
    @Transactional
    @Query("SELECT COUNT(p) FROM Phone p WHERE p.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);


    /**
     * Проверяет, существует ли телефонный номер, связанный с определенным пользователем.
     *
     * @param phone   номер телефона
     * @param userId  идентификатор пользователя
     * @return true, если телефон уже существует для данного пользователя, false в противном случае
     */
    @Transactional
    boolean existsByPhoneAndUserId(String phone, Long userId);

    /**
     * Проверяет, существует ли указанный телефонный номер среди всех зарегистрированных.
     *
     * @param phone номер телефона
     * @return true, если телефон уже существует, false в противном случае
     */
    @Transactional
    boolean existsByPhone(String phone);

    /**
     * Удаляет телефонный номер по его значению.
     *
     * @param phone номер телефона, подлежащий удалению
     */
    @Transactional
    void deleteByPhone(String phone);

    /**
     * Обновляет существующий телефонный номер в базе данных.
     *
     * @param newPhone новый номер телефона
     */
    @Transactional
    @Modifying
    @Query("UPDATE Phone p SET p.phone = :newPhone WHERE p.phone = :newPhone")
    void updatePhone(String newPhone);
}
