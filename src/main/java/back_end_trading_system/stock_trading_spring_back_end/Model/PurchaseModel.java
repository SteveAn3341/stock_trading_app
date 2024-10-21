package back_end_trading_system.stock_trading_spring_back_end.Model;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
public class PurchaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String stockSymbol;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private Long userId; // Assuming you're tracking user purchases

    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    public PurchaseModel() {
    }

    public PurchaseModel(String stockSymbol, int quantity, double price, Long userId) {
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
        this.userId = userId;
        this.purchaseDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
// Getters and Setters
    // Add additional fields if needed, like total price, etc.
}