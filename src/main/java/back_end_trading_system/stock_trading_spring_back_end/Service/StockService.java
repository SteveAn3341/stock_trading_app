package back_end_trading_system.stock_trading_spring_back_end.Service;

import back_end_trading_system.stock_trading_spring_back_end.Model.Stock;
import back_end_trading_system.stock_trading_spring_back_end.dto.StockDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class StockService {

    @Value("${api.alphavantage.url}")
    private String finnhubUrl;

    @Value("${api.alphavantage.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ExternalApiClient externalApiClient;


    public List<Stock> getStockDetailsForMultipleCompanies(List<String> symbols) {
        List<Stock> stockList = new ArrayList<>();

        for (String symbol : symbols) {
            // Build the API request URL
            String url = String.format("%s?symbol=%s&token=%s", finnhubUrl, symbol, apiKey);

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Call the API and log the response
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            // Log the API response
            System.out.println("API Response for symbol " + symbol + ": " + responseBody);

            if (responseBody != null && responseBody.get("c") != null) {
                // Create and populate the Stock object with available data
                Stock stock = new Stock();
                stock.setSymbol(symbol);
                stock.setName(symbol); // You can set a real name if needed
                stock.setCurrentPrice(Double.parseDouble(responseBody.get("c").toString()));  // Current price
                stock.setOpenPrice(Double.parseDouble(responseBody.get("o").toString()));    // Open price

                stockList.add(stock); // Add stock to the list
            } else {
                // Log a warning and skip the symbol
                System.out.println("No stock data found for symbol: " + symbol);
            }
        }

        return stockList;
    }
    public StockDetails getStockDetails(String symbol) {
        try {
            return externalApiClient.getStockDetails(symbol);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error fetching stock details for symbol " + symbol + ": " + e.getMessage());
            return null; // Or throw a custom exception to be handled by the controller
        }
    }

}