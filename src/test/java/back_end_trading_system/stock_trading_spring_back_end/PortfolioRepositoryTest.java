package back_end_trading_system.stock_trading_spring_back_end;

import back_end_trading_system.stock_trading_spring_back_end.Model.PortfolioModel;
import back_end_trading_system.stock_trading_spring_back_end.Model.User;
import back_end_trading_system.stock_trading_spring_back_end.Repository.PortfolioRepository;
import back_end_trading_system.stock_trading_spring_back_end.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class PortfolioRepositoryTest {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUserId() {
        // Given - setup data
        User user = new User();
        BigDecimal num2 = new BigDecimal("50.123");
        user.setBalance(num2);
        user.setUsername("testuser");
        user.setPassword("testpassword"); // Set a password for the user
        user.setTotalInvestment(new BigDecimal("100.00")); // Set a value for totalInvestment
        user = userRepository.save(user); // Save the user first and assign it back to user // Save the user first and assign it back to user

        PortfolioModel portfolio = new PortfolioModel();
        portfolio.setUser(user);   // Set the User object, not the userId
        portfolioRepository.save(portfolio); // Save the portfolio

        // When - execute the method under test
        PortfolioModel foundPortfolio = portfolioRepository.findByUserId(user.getId());

        // Then - assert the results
        assertThat(foundPortfolio).isNotNull();
        assertThat(foundPortfolio.getUser().getId()).isEqualTo(user.getId());
    }
}