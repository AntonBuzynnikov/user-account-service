package ru.buzynnikov.user_acount_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.buzynnikov.user_acount_service.models.Account;

import java.math.BigDecimal;

/**
 * Репозиторий для работы с моделью счетов (Account).
 * Поддерживает стандартные операции CRUD, специфичные запросы и хранимые процедуры.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a.balance FROM Account a WHERE a.user.id = :userId")
    BigDecimal findBalanceByUserId(Long userId);
    /**
     * Хранимый PL/PgSQL-процедура для массового обновления балансов всех счетов.
     * Процент начисляется относительно текущих остатков, ограничен максимальным процентом начисления.
     *
     * @param percent     начальный процент начисления процентов
     * @param maxPercent  максимальный допустимый процент начисления
     */
    @Transactional
    @Procedure("update_all_balances")
    void updateAllBalances(@Param("percent") int percent, @Param("max_percent") int maxPercent);

    /**
     * Извлекает баланс счета по идентификатору пользователя.
     * Транзакция имеет уровень изоляции SERIALIZABLE для избежания аномалий конкурентного доступа.
     *
     * @param id идентификатор пользователя
     * @return баланс счета пользователя
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Query("SELECT a.balance FROM Account a WHERE a.user.id = :id")
    BigDecimal getBalanceById(Long id);

    /**
     * Высчитывает указанную сумму с баланса счета определенного пользователя.
     * Уровень изоляции транзакции - SERIALIZABLE, чтобы предотвратить конфликты параллельных операций.
     *
     * @param fromId идентификатор пользователя, чей баланс уменьшается
     * @param amount сумма списания
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance - :amount WHERE a.user.id = :fromId")
    void subtractBalance(Long fromId, BigDecimal amount);

    /**
     * Пополняет баланс счета определенного пользователя указанной суммой.
     * Уровень изоляции транзакции - SERIALIZABLE, чтобы исключить конфликты параллельного доступа.
     *
     * @param toId   идентификатор пользователя, чью сумму пополняют
     * @param amount сумма пополнения
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.user.id = :toId")
    void addBalance(Long toId, BigDecimal amount);
}
