package back_end_trading_system.stock_trading_spring_back_end.Repository;

import back_end_trading_system.stock_trading_spring_back_end.Model.PurchaseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface PurchaseRepository extends JpaRepository<PurchaseModel, Long> {
    List<PurchaseModel> findByUserId(Long userId);

    // New method to find a purchase by stock symbol and user ID
    List<PurchaseModel> findByUserIdAndStockSymbol(Long userId, String stockSymbol);
}