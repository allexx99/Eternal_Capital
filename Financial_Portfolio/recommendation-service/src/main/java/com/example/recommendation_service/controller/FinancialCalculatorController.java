package com.example.recommendation_service.controller;

import com.example.recommendation_service.Structures.EarnFee;
import com.example.recommendation_service.Structures.MonteCarloResults;
import com.example.recommendation_service.service.impl.FinancialCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class FinancialCalculatorController {

    private final FinancialCalculatorService financialCalculatorService;

    @PostMapping(value = "/futureValue")
    public ResponseEntity<List<EarnFee>> futureValue(@RequestParam float principal,
                                                     @RequestParam float monthlyContribution,
                                                     @RequestParam float annualReturnRate,
                                                     @RequestParam float annualManagementFee,
                                                     @RequestParam int investmentPeriod) {
        List<EarnFee> earnFee = financialCalculatorService.futureValue(principal, monthlyContribution, annualReturnRate, annualManagementFee, investmentPeriod);
        return ResponseEntity.ok(earnFee);
    }

    @PostMapping(value = "/calculateMonthlySavings")
    public ResponseEntity<Double> calculateMonthlySavings(@RequestParam double goalAmount,
                                                          @RequestParam double annualRate,
                                                          @RequestParam int years) {
        double monthlySavings = financialCalculatorService.calculateMonthlySavings(goalAmount, annualRate, years);
        return ResponseEntity.ok(monthlySavings);
    }

    @PostMapping(value = "/monteCarloSimulation")
    public ResponseEntity<MonteCarloResults> runSimulation(@RequestParam double initialAmount,
                                                           @RequestParam double meanReturn,
                                                           @RequestParam double volatility,
                                                           @RequestParam int years,
                                                           @RequestParam double inflationRate,
                                                           @RequestParam int simulations) {
        MonteCarloResults monteCarloResults = financialCalculatorService.monteCarloSimulation(initialAmount, meanReturn, volatility, years, inflationRate, simulations);
        return ResponseEntity.ok(monteCarloResults);
    }
}
