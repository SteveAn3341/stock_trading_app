package back_end_trading_system.stock_trading_spring_back_end.dto;

import back_end_trading_system.stock_trading_spring_back_end.Model.PurchaseModel;

import java.util.List;

public class PortfolioDTO {
    private List<PurchaseModel> purchases;

    public PortfolioDTO(List<PurchaseModel> purchases) {
        this.purchases = purchases;
    }

    public List<PurchaseModel> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseModel> purchases) {
        this.purchases = purchases;
    }
}