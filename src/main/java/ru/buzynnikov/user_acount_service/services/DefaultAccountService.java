package ru.buzynnikov.user_acount_service.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.buzynnikov.user_acount_service.aspect.TransferLog;
import ru.buzynnikov.user_acount_service.exceptions.NotEnoughMoneyException;
import ru.buzynnikov.user_acount_service.repositories.AccountRepository;
import ru.buzynnikov.user_acount_service.services.interfasces.AccountService;
import ru.buzynnikov.user_acount_service.services.interfasces.UserService;

import java.math.BigDecimal;

/**
 * Реализация сервисов по управлению счетами пользователей.
 * Обеспечивает регулярные обновления баланса и переводы средств между счетами.
 */
@Service
public class DefaultAccountService implements AccountService {

    /**
     * Интервал обновления всех балансов (30 секунд).
     */
    private static final long UPDATE_INTERVAL = 30000L;

    /**
     * Значение процента обновления баланса (берется из конфигурационного файла).
     */
    @Value("${account.update.percent}")
    private Integer updatePercent;

    /**
     * Максимально допустимая сумма на балансе в процентах от начальной суммы (берется из конфигурационного файла).
     */
    @Value("${account.update.max-balance}")
    private Integer maxBalance;

    private final AccountRepository accountRepository;
    private final UserService userService;

    public DefaultAccountService(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    /**
     * Планировщик регулярных обновлений всех балансов пользователей.
     * Запускается каждые 30 секунд после начальной задержки.
     */
    @Transactional
    @Scheduled(fixedRate = UPDATE_INTERVAL, initialDelay = UPDATE_INTERVAL)
    @Override
    public void updateAllBalances() {
        accountRepository.updateAllBalances(updatePercent, maxBalance);
    }

    /**
     * Метод для перевода средств с одного счета на другой.
     *
     * @param fromId идентификатор отправляющего пользователя
     * @param toId   идентификатор получающего пользователя
     * @param amount сумма перевода
     */
    @TransferLog
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void transferTo(Long fromId, Long toId, BigDecimal amount) {
        validateUser(fromId);
        validateUser(toId);
        validateBalance(amount, fromId);
        accountRepository.subtractBalance(fromId, amount);
        accountRepository.addBalance(toId, amount);
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        validateUser(userId);
        return accountRepository.findBalanceByUserId(userId);
    }

    /**
     * Проверяет, достаточно ли средств на счете отправителя.
     *
     * @param amount сумма перевода
     * @param fromId идентификатор отправляющего пользователя
     * @throws NotEnoughMoneyException если недостаточно средств на счете
     */
    private void validateBalance(BigDecimal amount, Long fromId) {
        if (amount.compareTo(accountRepository.getBalanceById(fromId)) > 0)
            throw new NotEnoughMoneyException(String.format("Недостаточно средств на счете %s", fromId));
    }

    /**
     * Проверяет, существует ли пользователь с указанным идентификатором.
     *
     * @param userId идентификатор пользователя
     */
    private void validateUser(Long userId) {
        userService.isUserExists(userId);
    }



}
