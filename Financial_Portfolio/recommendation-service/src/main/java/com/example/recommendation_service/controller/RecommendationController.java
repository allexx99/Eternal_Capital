package com.example.recommendation_service.controller;

import com.example.recommendation_service.model.BETIndex;
import com.example.recommendation_service.model.Portfolio;
import com.example.recommendation_service.model.Stock;
import com.example.recommendation_service.service.impl.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping(value = "/balanceStocks/{investorId}/{portfolioId}")
    public ResponseEntity<String> balanceStocks(@RequestBody List<Stock> stocks,
                                                @PathVariable long investorId,
                                                @PathVariable long portfolioId,
                                                @RequestParam float investedSum,
                                                @RequestParam float tradingFee,
                                                @RequestParam float fixedFee,
                                                @RequestParam float minTransaction) {
        if(recommendationService.balanceStocks(investorId, portfolioId, stocks, investedSum, tradingFee, fixedFee, minTransaction)) {
            return ResponseEntity.ok("Stocks balanced successfully!");
        } else {
            return ResponseEntity.ok("Stocks couldn't be balanced!");
        }
    }
}
