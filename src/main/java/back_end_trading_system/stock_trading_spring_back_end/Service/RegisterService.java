package back_end_trading_system.stock_trading_spring_back_end.Service;

import back_end_trading_system.stock_trading_spring_back_end.Model.User;
import back_end_trading_system.stock_trading_spring_back_end.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(User user) {
        // Check if the user already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "Username already exists.";
        }

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the new user
        userRepository.save(user);

        return "User registered successfully!";
    }
}