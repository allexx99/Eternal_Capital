package com.example.recommendation_service.controller;

import com.example.recommendation_service.model.Portfolio;
import com.example.recommendation_service.model.Stock;
import com.example.recommendation_service.service.impl.InvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.Port;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class InvestorController {

    private final InvestorService investorService;

    @Autowired
    public InvestorController(InvestorService investorService) {
        this.investorService = investorService;
    }

    @PostMapping(value = "/addInvestor/{id}")
    public void addInvestor(@PathVariable long id) {
        investorService.addInvestor(id);
    }

    @DeleteMapping(value = "/deleteInvestor/{id}")
    public void deleteInvestor(@PathVariable long id) {
        investorService.deleteInvestor(id);
    }

    @GetMapping(value = "/getInvestors")
    public void getInvestors() {
        investorService.getInvestors();
    }

    @PostMapping(value = "/addPortfolio/{investorId}")
    public void addPortfolio(@PathVariable long investorId) {
        investorService.addPortfolio(investorId);
    }

    @GetMapping(value = "/getPortfolios/{investorId}")
    public ResponseEntity<List<Portfolio>> getPortfolios(@PathVariable long investorId) {
        List<Portfolio> portfolios = investorService.getPortfolios(investorId);
        return new ResponseEntity<>(portfolios, HttpStatus.OK);
    }

    @PostMapping(value = "/addStocksToPortfolio/{investorId}/{portfolioId}")
    public void addStocksToPortfolio(@PathVariable long investorId, @PathVariable long portfolioId, @RequestBody List<Stock> stocks) {
        investorService.addStocksToPortfolio(investorId, portfolioId, stocks);
    }

    @DeleteMapping(value = "/deletePortfolio/{investorId}/{portfolioId}")
    public void deletePortfolio(@PathVariable long investorId, @PathVariable long portfolioId) {
        investorService.deletePortfolio(investorId, portfolioId);
    }
}
