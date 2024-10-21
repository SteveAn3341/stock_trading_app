package back_end_trading_system.stock_trading_spring_back_end.Controller;

import back_end_trading_system.stock_trading_spring_back_end.Model.Stock;
import back_end_trading_system.stock_trading_spring_back_end.Service.StockService;
import back_end_trading_system.stock_trading_spring_back_end.dto.StockDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/stocks")
public class StockController {
    private static final Logger logger = LoggerFactory.getLogger(StockController.class);
    @Autowired
    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/details/multiple")
    public ResponseEntity<List<Stock>> getStockDetailsForMultipleCompanies(@RequestParam List<String> symbols) {
        List<Stock> stocks = stockService.getStockDetailsForMultipleCompanies(symbols);
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/details/{stockSymbol}")
    public ResponseEntity<StockDetails> getStockDetails(@PathVariable String stockSymbol) {
        try {
            StockDetails stockDetails = stockService.getStockDetails(stockSymbol);
            return ResponseEntity.ok(stockDetails);
        } catch (Exception e) {
            // Log the error for debugging
            logger.error("Error fetching stock details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}