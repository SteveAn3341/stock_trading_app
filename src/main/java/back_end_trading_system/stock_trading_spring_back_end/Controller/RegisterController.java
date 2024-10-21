package back_end_trading_system.stock_trading_spring_back_end.Controller;

import back_end_trading_system.stock_trading_spring_back_end.Model.User;
import back_end_trading_system.stock_trading_spring_back_end.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService userService;

    @PostMapping({"/register", "/api/register"})
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        logger.info("Received registration request for user: {}", user.getUsername());
        try {
            User registeredUser = userService.registerUser(user);
            logger.info("User registered successfully: {}", registeredUser.getUsername());
            return ResponseEntity.ok("User registered successfully: " + registeredUser.getUsername());
        } catch (Exception e) {
            logger.error("Registration failed for user: {}", user.getUsername(), e);
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
}