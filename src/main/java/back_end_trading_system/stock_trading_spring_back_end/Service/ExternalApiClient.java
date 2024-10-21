package back_end_trading_system.stock_trading_spring_back_end.Service;

import back_end_trading_system.stock_trading_spring_back_end.dto.StockDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ExternalApiClient {
    @Value("${api.alphavantage.url}")
    private String finnhubUrl;

    @Value("${api.alphavantage.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ExternalApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public StockDetails getStockDetails(String symbol) {
        String url = String.format("%s?symbol=%s&token=%s", finnhubUrl, symbol, apiKey);
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null) {
            StockDetails stockDetails = new StockDetails();
            stockDetails.setSymbol(symbol);
            stockDetails.setCurrentPrice(Double.parseDouble(response.get("c").toString()));
            return stockDetails;
        }
        return null; // Handle the case where the response is null
    }
    }