package back_end_trading_system.stock_trading_spring_back_end.Controller;
import back_end_trading_system.stock_trading_spring_back_end.dto.BalanceResponse;

import back_end_trading_system.stock_trading_spring_back_end.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/{userId}/balance")
    public ResponseEntity<?> updateBalance(@PathVariable Long userId, @RequestBody Map<String, BigDecimal> request) {
        if (!request.containsKey("amount")) {
            return ResponseEntity.badRequest().body("Amount is required.");
        }

        BigDecimal amount = request.get("amount");
        if (amount == null) {
            return ResponseEntity.badRequest().body("Amount cannot be null.");
        }

        BigDecimal newBalance = userService.updateBalance(userId, amount);
        return ResponseEntity.ok(new BalanceResponse(newBalance));
    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable Long userId) {
        try {
            BigDecimal balance = userService.getBalance(userId);
            return ResponseEntity.ok(new BalanceResponse(balance));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
