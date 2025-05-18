package ru.buzynnikov.user_acount_service.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.buzynnikov.user_acount_service.dto.AccountCreateRequest;
import ru.buzynnikov.user_acount_service.dto.AccountDTO;
import ru.buzynnikov.user_acount_service.repositories.AccountRepository;
import ru.buzynnikov.user_acount_service.services.interfasces.AccountService;

import java.math.BigDecimal;


@Service
public class DefaultAccountService implements AccountService {

    private static final long UPDATE_INTERVAL = 30000L;

    @Value("${account.update.percent}")
    private Integer updatePercent;

    @Value("${account.update.max-balance}")
    private Integer maxBalance;

    private final AccountRepository accountRepository;

    public DefaultAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Scheduled(fixedRate = UPDATE_INTERVAL)
    @Override
    public void updateAllBalances() {
        accountRepository.updateAllBalances(updatePercent, maxBalance, 30);
    }

    @Override
    public void transferTo(Long fromId, Long toId, BigDecimal amount) {

    }

}
