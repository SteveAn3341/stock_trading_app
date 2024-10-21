package back_end_trading_system.stock_trading_spring_back_end.Model;

import jakarta.persistence.*;

@Entity
public class PortfolioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private double totalInvested; // Field to track total investment

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) { // Changed method name here
        this.user = user;
    }

    public double getTotalInvested() {
        return totalInvested;
    }

    public void setTotalInvested(double totalInvested) {
        this.totalInvested = totalInvested;
    }
}