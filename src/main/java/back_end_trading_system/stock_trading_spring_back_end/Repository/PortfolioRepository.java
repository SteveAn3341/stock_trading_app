package back_end_trading_system.stock_trading_spring_back_end.Repository;


import back_end_trading_system.stock_trading_spring_back_end.Model.PortfolioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioModel, Long> {
    PortfolioModel findByUserId(Long userId);


}

