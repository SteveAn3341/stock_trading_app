package back_end_trading_system.stock_trading_spring_back_end;

import back_end_trading_system.stock_trading_spring_back_end.Model.PurchaseModel;
import back_end_trading_system.stock_trading_spring_back_end.Model.User;
import back_end_trading_system.stock_trading_spring_back_end.Repository.PurchaseRepository;
import back_end_trading_system.stock_trading_spring_back_end.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class PurchaseRepositoryTest {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUserIdAndStockSymbol() {
        // Given - setup data
        User user = new User();
        user.setUsername("testuser");
        user.setBalance(new BigDecimal("1000.00"));
        user.setPassword("testpassword");
        user.setTotalInvestment(new BigDecimal("5000.00"));
        user = userRepository.save(user); // Save user

        PurchaseModel purchase = new PurchaseModel();
        purchase.setUserId(user.getId());
        purchase.setStockSymbol("AAPL");
        purchase.setQuantity(10);
        purchase.setPrice(150.00);
        purchase.setPurchaseDate(LocalDateTime.now()); // Set purchaseDate
        purchaseRepository.save(purchase); // Save the purchase

        // When - execute the method under test
        List<PurchaseModel> foundPurchases = purchaseRepository.findByUserIdAndStockSymbol(user.getId(), "AAPL");

        // Then - assert the results
        assertThat(foundPurchases).isNotEmpty();
        assertThat(foundPurchases.get(0).getStockSymbol()).isEqualTo("AAPL");
        assertThat(foundPurchases.get(0).getUserId()).isEqualTo(user.getId());
    }
}
