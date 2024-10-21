package back_end_trading_system.stock_trading_spring_back_end.Controller;

import back_end_trading_system.stock_trading_spring_back_end.Model.PortfolioModel;
import back_end_trading_system.stock_trading_spring_back_end.Service.PortfolioService;
import back_end_trading_system.stock_trading_spring_back_end.dto.PortfolioDTO;
import back_end_trading_system.stock_trading_spring_back_end.dto.PurchaseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {
    private static final Logger logger = LoggerFactory.getLogger(PortfolioController.class);
    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping("/buy")
    public ResponseEntity<?> buyStock(@RequestBody PurchaseRequest purchaseRequest) {
        logger.info("Received buy stock request: {}", purchaseRequest);
        try {
            portfolioService.buyStock(purchaseRequest);
            return ResponseEntity.ok("Stock purchased successfully");
        } catch (IllegalArgumentException e) {
            logger.error("Invalid request: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Error processing request: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PortfolioDTO> getPortfolio(@PathVariable Long userId) {
        PortfolioDTO portfolioDTO = portfolioService.getPortfolio(userId);
        return ResponseEntity.ok(portfolioDTO);
    }


    @PostMapping("/sell/{symbol}")
    public ResponseEntity<?> sellStock(
            @PathVariable String symbol,
            @RequestParam Long userId,
            @RequestParam int quantity) {
        try {
            BigDecimal salePrice = portfolioService.sellStock(symbol, userId, quantity);

            // Ensure salePrice is returned to the frontend
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Stock sold successfully");
            response.put("salePrice", salePrice);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error selling stock: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error selling stock: " + e.getMessage(), e);
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }


}
