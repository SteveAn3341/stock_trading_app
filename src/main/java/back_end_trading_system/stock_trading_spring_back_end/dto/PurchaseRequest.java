package back_end_trading_system.stock_trading_spring_back_end.dto;
public class PurchaseRequest {
    private String stockSymbol;
    private int quantity;
    private double price;
    private Long userId;

    public String getStockSymbol() {
        return stockSymbol;
    }

    public PurchaseRequest() {

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}