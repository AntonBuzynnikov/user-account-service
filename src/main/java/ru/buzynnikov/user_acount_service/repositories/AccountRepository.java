package ru.buzynnikov.user_acount_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.buzynnikov.user_acount_service.models.Account;
import ru.buzynnikov.user_acount_service.dto.AccountDTO;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT new ru.buzynnikov.user_acount_service.dto.AccountDTO(a.id, a.balance, a.initialBalance, a.lastUpdated) FROM Account a")
    List<AccountDTO> getAllAccounts();

    @Procedure("update_all_balances")
    void updateAllBalances(@Param("percent") int percent, @Param("max_percent") int maxPercent, @Param("seconds") int seconds);
}
