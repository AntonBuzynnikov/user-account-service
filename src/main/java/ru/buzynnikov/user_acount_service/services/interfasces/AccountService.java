package ru.buzynnikov.user_acount_service.services.interfasces;


import java.math.BigDecimal;

public interface AccountService {

    void updateAllBalances();

    void transferTo(Long fromId, Long toId, BigDecimal amount);


    BigDecimal getBalance(Long userId);
}

