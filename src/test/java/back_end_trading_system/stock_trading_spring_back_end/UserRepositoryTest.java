package back_end_trading_system.stock_trading_spring_back_end;

import back_end_trading_system.stock_trading_spring_back_end.Model.User;
import back_end_trading_system.stock_trading_spring_back_end.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        // Given - setup data
        User user = new User();
        user.setUsername("testuser");
        user.setBalance(new BigDecimal("1000.00"));
        user.setPassword("testpassword");
        user.setTotalInvestment(new BigDecimal("5000.00"));
        user = userRepository.save(user); // Save user


        // When - execute the method under test
        User foundUser = userRepository.findByUsername("testuser");

        // Then - assert the results
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testFindByUsername_NotFound() {
        // Given - no user saved

        // When - execute the method under test
        User foundUser = userRepository.findByUsername("nonexistentuser");

        // Then - assert the results
        assertThat(foundUser).isNull(); // User should not be found
    }
}