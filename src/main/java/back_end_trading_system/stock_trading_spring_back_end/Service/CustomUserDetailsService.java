package back_end_trading_system.stock_trading_spring_back_end.Service;

import back_end_trading_system.stock_trading_spring_back_end.Model.User;
import back_end_trading_system.stock_trading_spring_back_end.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Attempting to load user by username: {}", username);

        User user = userRepository.findByUsername(username);

        if (user == null) {
            logger.error("User not found: {}", username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        logger.debug("User found: {}", username);
        logger.debug("User password hash: {}", user.getPassword());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}