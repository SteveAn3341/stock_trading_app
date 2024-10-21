package back_end_trading_system.stock_trading_spring_back_end.Service;
import back_end_trading_system.stock_trading_spring_back_end.dto.PortfolioDTO;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import back_end_trading_system.stock_trading_spring_back_end.Model.PortfolioModel;
import back_end_trading_system.stock_trading_spring_back_end.Model.PurchaseModel;
import back_end_trading_system.stock_trading_spring_back_end.Model.User;
import back_end_trading_system.stock_trading_spring_back_end.Repository.PortfolioRepository;
import back_end_trading_system.stock_trading_spring_back_end.Repository.PurchaseRepository;
import back_end_trading_system.stock_trading_spring_back_end.Repository.UserRepository;
import back_end_trading_system.stock_trading_spring_back_end.dto.PurchaseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class PortfolioService {
    private static final Logger logger = LoggerFactory.getLogger(PortfolioService.class);
    private final PurchaseRepository purchaseRepository;
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    @Autowired
    private UserService userService;



    @Autowired
    public PortfolioService(PurchaseRepository purchaseRepository,
                            PortfolioRepository portfolioRepository,
                            UserRepository userRepository) {
        this.purchaseRepository = purchaseRepository;
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
    }



    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null); // Correct usage
    }


    public void buyStock(PurchaseRequest purchaseRequest) {
        // Create a new purchase entry


        logger.info("Buying stock: {}", purchaseRequest);

        if (purchaseRequest.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User user = getUserById(purchaseRequest.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + purchaseRequest.getUserId());
        }
        PurchaseModel purchase = new PurchaseModel(
                purchaseRequest.getStockSymbol(),
                purchaseRequest.getQuantity(),
                purchaseRequest.getPrice(),
                purchaseRequest.getUserId()
        );

        System.out.println("Creating Purchase: " + purchase);
        purchaseRepository.save(purchase);


        // Retrieve the user by userId

        // Update the user's portfolio
        PortfolioModel portfolio = portfolioRepository.findByUserId(purchaseRequest.getUserId());

        if (portfolio == null) {
            // Create a new portfolio if it doesn't exist
            portfolio = new PortfolioModel();
            portfolio.setUser(user); // Set the existing user
            portfolio.setTotalInvested(purchaseRequest.getPrice() * purchaseRequest.getQuantity());
            portfolioRepository.save(portfolio);
        } else {
            // Update the existing portfolio with the new purchase data
            double newTotal = portfolio.getTotalInvested() + (purchaseRequest.getPrice() * purchaseRequest.getQuantity());
            portfolio.setTotalInvested(newTotal);
            portfolioRepository.save(portfolio);
        }
    }





    public PortfolioDTO getPortfolio(Long userId) {
        List<PurchaseModel> purchases = purchaseRepository.findByUserId(userId);
        return new PortfolioDTO(purchases);
    }



    @Transactional
    public BigDecimal sellStock(String symbol, Long userId, int quantityToSell) {
        logger.info("Attempting to sell {} shares of stock: {} for user: {}", quantityToSell, symbol, userId);
        List<PurchaseModel> purchases = purchaseRepository.findByUserIdAndStockSymbol(userId, symbol);

        if (purchases.isEmpty()) {
            logger.warn("No stocks found for symbol {} and user {}", symbol, userId);
            throw new RuntimeException("No stocks found for the given symbol and user");
        }

        int totalQuantityOwned = purchases.stream().mapToInt(PurchaseModel::getQuantity).sum();
        if (quantityToSell > totalQuantityOwned) {
            throw new RuntimeException("Not enough shares to sell. Owned: " + totalQuantityOwned + ", Attempted to sell: " + quantityToSell);
        }

        BigDecimal totalValueToSell = BigDecimal.ZERO;  // Use BigDecimal for precise calculation
        int remainingToSell = quantityToSell;

        for (PurchaseModel purchase : purchases) {
            if (remainingToSell == 0) break;

            int quantityFromThisPurchase = Math.min(purchase.getQuantity(), remainingToSell);
            BigDecimal pricePerUnit = BigDecimal.valueOf(purchase.getPrice());
            BigDecimal valueFromThisPurchase = pricePerUnit.multiply(BigDecimal.valueOf(quantityFromThisPurchase));

            totalValueToSell = totalValueToSell.add(valueFromThisPurchase);
            remainingToSell -= quantityFromThisPurchase;

            if (quantityFromThisPurchase == purchase.getQuantity()) {
                purchaseRepository.delete(purchase);
            } else {
                purchase.setQuantity(purchase.getQuantity() - quantityFromThisPurchase);
                purchaseRepository.save(purchase);
            }
        }

        logger.info("Successfully sold {} shares of stock: {} for user: {}, total value: ${}",
                quantityToSell, symbol, userId, totalValueToSell);

        // Update user's balance with the total value of the stock sold
        userService.updateBalance(userId, totalValueToSell);

        // Return the total value sold to the controller
        return totalValueToSell;
    }


}