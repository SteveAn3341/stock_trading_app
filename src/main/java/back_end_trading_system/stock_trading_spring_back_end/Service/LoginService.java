package back_end_trading_system.stock_trading_spring_back_end.Service;

import back_end_trading_system.stock_trading_spring_back_end.Model.User;
import back_end_trading_system.stock_trading_spring_back_end.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> login(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            User user = userRepository.findByUsername(username);

            Map<String, Object> result = new HashMap<>();
            result.put("message", "Login successful for user: " + authentication.getName());
            result.put("username", authentication.getName());
            if (user != null) {
                result.put("userId", user.getId());
            }
            return result;
        } catch (AuthenticationException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("message", "Login failed: " + e.getMessage());
            return result;
        }
    }
}