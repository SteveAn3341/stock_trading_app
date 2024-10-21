package back_end_trading_system.stock_trading_spring_back_end.dto;

import java.math.BigDecimal;

public class BalanceResponse {
    private BigDecimal balance;

    public BalanceResponse(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
