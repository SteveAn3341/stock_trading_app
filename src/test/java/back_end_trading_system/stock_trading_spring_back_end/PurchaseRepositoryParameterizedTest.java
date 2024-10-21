package back_end_trading_system.stock_trading_spring_back_end;

import back_end_trading_system.stock_trading_spring_back_end.Model.PurchaseModel;
import back_end_trading_system.stock_trading_spring_back_end.Model.User;
import back_end_trading_system.stock_trading_spring_back_end.Repository.PurchaseRepository;
import back_end_trading_system.stock_trading_spring_back_end.Repository.UserRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class PurchaseRepositoryParameterizedTest {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @CsvSource({
            "AAPL, 1",
            "GOOGL, 2",
            "TSLA, 3"
    })
    public void testFindByUserIdAndStockSymbolParameterized(String stockSymbol, Long userId) {
        // Given - create and save a user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setBalance(new BigDecimal("1000.00"));
        user.setTotalInvestment(new BigDecimal("5000.00"));

        // Save the user
        user = userRepository.save(user);

        // Given - create a new purchase with a valid purchaseDate
        PurchaseModel purchase = new PurchaseModel();
        purchase.setUserId(user.getId());
        purchase.setStockSymbol("AAPL");
        purchase.setQuantity(10);
        purchase.setPrice(150.00);
        purchase.setPurchaseDate(LocalDateTime.now());  // Ensure purchaseDate is set
        purchaseRepository.save(purchase);

        // When - execute the method under test
        List<PurchaseModel> foundPurchases = purchaseRepository.findByUserIdAndStockSymbol(user.getId(), "AAPL");

        // Then - assert the results
        assertThat(foundPurchases).isNotEmpty();
        assertThat(foundPurchases.get(0).getStockSymbol()).isEqualTo("AAPL");
        assertThat(foundPurchases.get(0).getUserId()).isEqualTo(user.getId());
    }
}