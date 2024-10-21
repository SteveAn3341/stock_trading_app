package back_end_trading_system.stock_trading_spring_back_end.dto;

public class StockDetails {
    private String symbol;
    private double currentPrice;
    // Add other fields as necessary, along with getters and setters

    public StockDetails() {
    }

    // Constructor
    public StockDetails(String symbol, double currentPrice) {
        this.symbol = symbol;
        this.currentPrice = currentPrice;
    }

    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
}