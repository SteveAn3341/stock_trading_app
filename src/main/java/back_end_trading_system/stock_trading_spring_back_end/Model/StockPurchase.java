package back_end_trading_system.stock_trading_spring_back_end.Model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class StockPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private PortfolioModel portfolioModel;

    private String stockSymbol;
    private Integer quantity;
    private BigDecimal purchasePrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PortfolioModel getPortfolio() {
        return portfolioModel;
    }

    public void setPortfolio(PortfolioModel portfolioModel) {
        this.portfolioModel = portfolioModel;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
// Getters and setters
}
