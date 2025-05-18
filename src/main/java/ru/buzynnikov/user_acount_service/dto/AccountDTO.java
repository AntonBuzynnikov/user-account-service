package ru.buzynnikov.user_acount_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountDTO {
    private Long id;
    private BigDecimal balance;
    private BigDecimal initialBalance;
    private LocalDateTime lastUpdated;

    public AccountDTO() {
    }

    public AccountDTO(Long id, BigDecimal balance, BigDecimal initialBalance, LocalDateTime lastUpdated) {
        this.id = id;
        this.balance = balance;
        this.initialBalance = initialBalance;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public synchronized void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
