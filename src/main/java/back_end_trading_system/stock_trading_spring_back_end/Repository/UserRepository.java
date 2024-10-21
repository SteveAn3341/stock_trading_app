package back_end_trading_system.stock_trading_spring_back_end.Repository;


import back_end_trading_system.stock_trading_spring_back_end.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA will automatically implement this method
    User findByUsername(String username);
}