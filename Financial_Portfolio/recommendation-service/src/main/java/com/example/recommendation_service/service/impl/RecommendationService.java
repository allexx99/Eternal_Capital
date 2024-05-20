package com.example.recommendation_service.service.impl;

import com.example.recommendation_service.model.Investor;
import com.example.recommendation_service.model.Portfolio;
import com.example.recommendation_service.model.Stock;
import com.example.recommendation_service.repository.InvestorRepo;
import com.example.recommendation_service.repository.PortfolioRepo;
import com.example.recommendation_service.repository.StockRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class RecommendationService {

    private final StockRepo stockRepo;
    private final InvestorRepo investorRepo;
    private final PortfolioRepo portfolioRepo;

    public boolean balanceStocks(long investorId, long portfolioId, List<Stock> stocks, float investedSum, float tradingFee, float fixedFee, float minTransaction) {

        // rewrite the stocks
        // stockRepo.deleteAll();

        Investor investor = investorRepo.findInvestorById(investorId);
        Portfolio portfolio = portfolioRepo.findPortfolioById(portfolioId);

        System.out.println(portfolioId);

        // --------------------- / old stock saves / --------------------- //

        /*stockRepo.deleteStocksByPortfolioId(portfolioId);

        for (Stock stock : stocks) {
            Stock newStock = new Stock();
            newStock.setBetIndex(stock.getBetIndex());
            newStock.setQuantity(stock.getQuantity()); // Retain quantity information
            newStock.setNormWeight(stock.getNormWeight()); // Retain normWeight information
            newStock.setPortfolio(portfolio);
            portfolio.getStocks().add(newStock);
        }

        investorRepo.save(investor);*/

        // --------------------- / old stock saves / --------------------- //

        // --------------------- / new stock saves / --------------------- //

        List<Stock> currentPortfolioStocks = portfolio.getStocks();

        if(!currentPortfolioStocks.isEmpty()) {
            for (Stock currentStock : currentPortfolioStocks) {
                for (Stock stock : stocks) {
                    if (currentStock.getBetIndex().getSymbol().equals(stock.getBetIndex().getSymbol())) {
                        currentStock.setQuantity(stock.getQuantity());
                        currentStock.setNormWeight(stock.getNormWeight());
                        currentStock.setBetIndex(stock.getBetIndex());
                        break;
                    }
                }
            }
        } else {
            for (Stock stock : stocks) {
                Stock newStock = new Stock();
                newStock.setBetIndex(stock.getBetIndex());
                newStock.setQuantity(stock.getQuantity());
                newStock.setNormWeight(stock.getNormWeight());
                newStock.setPortfolio(portfolio);
                portfolio.getStocks().add(newStock);
            }
        }


        investorRepo.save(investor);

        // --------------------- / new stock saves / --------------------- //

        // --------------------- // --------------------- //

        // if investedSum < minTransaction then the balance operation couldn't be performed.
        if(investedSum < minTransaction) {
            System.out.println("Invested sum is less than minimum transaction amount!");
            return false;
        }

        // --------------------- // --------------------- //

        // initialize some variables
        float origInvestedSum = investedSum;
        float totalWeight = 0;
        // float totalWeight = 0.0f;
        float portfolioValue = 0;

        // iterates over each stock in the portfolio and checks if the price and weight of each stock are set
        for (Stock stock : portfolio.getStocks()) {
            if (stock.getBetIndex().getPrice() == 0 || stock.getBetIndex().getWeight() == 0) {
                System.out.println("Price or weight not set for stock with symbol: " + stock.getBetIndex().getSymbol());
                return false;
            }
            totalWeight += stock.getBetIndex().getWeight();
            portfolioValue += stock.getBetIndex().getPrice() * stock.getQuantity();
        }

        /*// Limit totalWeight to two decimal places
        totalWeight = Float.parseFloat(String.format("%.2f", totalWeight));*/

        // --------------------- // --------------------- //

        // this line calculates the minimum count of each stock required to make a transaction of at least minTransaction
        // amount after considering trading and fixed fees
        HashMap<String, Integer> minCount = new HashMap<>();
        for(Stock stock : portfolio.getStocks()) {
            int minStockCount = (int) Math.ceil((minTransaction - fixedFee) / (1 + tradingFee) / stock.getBetIndex().getPrice());
            minCount.put(stock.getBetIndex().getSymbol(), minStockCount);
        }

        // --------------------- // --------------------- //

        // this recalculates the weight for the chosen stocks
        for(Stock stock : portfolio.getStocks()) {
            stock.setNormWeight(stock.getBetIndex().getWeight() / totalWeight);
            System.out.println("Symbol: " + stock.getBetIndex().getSymbol() + " Weight/TotalWeight: " + stock.getBetIndex().getWeight() + " / " + totalWeight + " = " + stock.getBetIndex().getWeight() / totalWeight);
            stockRepo.save(stock);
        }

        // --------------------- // --------------------- //

        // "for" creates all the possible combination of stocks of different lengths (the subsets).
        // then it iterates over the subsets of stocks and calculate for each stock in the subset
        // investedSum and totalInvested (of the stocks within a certain subset)
        // count is a dictionary that contains all the stocks from a subset along with the minCount
        // minCount = minimum quantity of stocks you can buy based on the fixed fees.
        HashMap<String,Integer> bestCount = null;
        Float bestError = null;
        List<List<Stock>> subsets = generateSubsets(portfolio.getStocks());
        for(List<Stock> subset : subsets) {
            HashMap<String, Integer> count = new HashMap<>();
            for(Stock stock : subset) {
                count.put(stock.getBetIndex().getSymbol(), minCount.get(stock.getBetIndex().getSymbol()));
            }

            investedSum = (origInvestedSum - fixedFee * subset.size()) / (1 + tradingFee);

            // calculate the total invested sum for the stocks in the subset
            float totalInvested = 0;
            for(Stock stock1 : subset) {
                totalInvested += stock1.getBetIndex().getPrice() * count.get(stock1.getBetIndex().getSymbol());
            }

            // checks if the total amount invested in the current subset of stocks is greater than
            // or equal to the adjusted invested sum. If true, it means that the desired investment amount has been met
            // or exceeded, so the loop continues to the next subset
            if(totalInvested >= investedSum) {
                continue;
            }

            // push on the h heap a tuple containing result from relativeError function and s (current stock)
            PriorityQueue<StockError> heap = new PriorityQueue<>(new StockComparator());
            for(Stock stock : subset) {
                heap.add(new StockError(stock, relativeError(stock, count, portfolioValue, investedSum)));
            }
            while(!heap.isEmpty()) {
                // pop the stockError object from the heap with the smallest relative error
                StockError stockError = heap.poll();

                // if totalInvested + the price of the current stock from the heap exceeds or equals the invested sum
                // then the program continues the while loop popping out the other remaining stocks.
                // Otherwise, if that sum is less than invested sum, that means that we can buy more of the current stock, and
                // the current stock is pushed back on the heap.
                if(totalInvested + stockError.getStock().getBetIndex().getPrice() >= investedSum) {
                    continue;
                }

                count.put(stockError.getStock().getBetIndex().getSymbol(), count.get(stockError.getStock().getBetIndex().getSymbol()) + 1);
                totalInvested += stockError.getStock().getBetIndex().getPrice();
                heap.add(new StockError(stockError.getStock(), relativeError(stockError.getStock(), count, portfolioValue, investedSum)));
            }
            float error = 0;
            float weight = 0;
            for(Stock stock : portfolio.getStocks()) {
                if(count.containsKey(stock.getBetIndex().getSymbol())) {
                    weight = stock.getBetIndex().getPrice() * (stock.getQuantity() + count.get(stock.getBetIndex().getSymbol())) / (portfolioValue + totalInvested);
                } else {
                    weight = stock.getBetIndex().getPrice() * (stock.getQuantity()) / (portfolioValue + totalInvested);
                }
                error += ((weight - stock.getNormWeight()) / stock.getNormWeight()) * ((weight - stock.getNormWeight()) / stock.getNormWeight());
            }
            if(bestError == null || error < bestError) {
                bestError = error;
                bestCount = count;
            }
        }
        if(bestCount == null) {
            return false;
        }
        for(Map.Entry<String,Integer> entry : bestCount.entrySet()) {
            for(Stock stock : portfolio.getStocks()) {
                if(stock.getBetIndex().getSymbol().equals(entry.getKey())) {
                    System.out.println("Symbol: " + stock.getBetIndex().getSymbol() + " Quantity: " + stock.getQuantity() + " Recommendation: " + entry.getValue());
                    stock.setQuantity(stock.getQuantity() + entry.getValue());
                    stockRepo.save(stock);
                    break;
                }
            }
        }
        return true;
    }

    public List<List<Stock>> generateSubsets(List<Stock> stocks) {
        List<List<Stock>> subsets = new ArrayList<>();
        generateSubsets(stocks, 0, new ArrayList<>(), subsets);
        return subsets;
    }

    private static void generateSubsets(List<Stock> stocks, int index, List<Stock> current, List<List<Stock>> subsets) {
        if(index == stocks.size()) {
            if(!current.isEmpty()) {
                subsets.add(new ArrayList<>(current));
            }
            return;
        }
        current.add(stocks.get(index));
        generateSubsets(stocks, index + 1, current, subsets);

        current.remove(current.size() - 1);
        generateSubsets(stocks, index + 1, current, subsets);
    }

    class StockError {
        Stock stock;
        float relativeError;

        // constructor
        public StockError(Stock stock, float relativeError) {
            this.stock = stock;
            this.relativeError = relativeError;
        }

        // getters
        public Stock getStock() {
            return this.stock;
        }

        public float getRelativeError() {
            return this.relativeError;
        }
    }

    class StockComparator implements Comparator<StockError> {
        public int compare(StockError s1, StockError s2) {
            return Float.compare(s1.getRelativeError(), s2.getRelativeError());
        }
    }

    public float relativeError(Stock stock, HashMap<String,Integer> count, float portfolioValue, float investedSum) {
        float weight = stock.getBetIndex().getPrice() * (stock.getQuantity() + count.get(stock.getBetIndex().getSymbol())) / (portfolioValue + investedSum);
        return (weight - stock.getNormWeight()) / stock.getNormWeight();
    }
}
