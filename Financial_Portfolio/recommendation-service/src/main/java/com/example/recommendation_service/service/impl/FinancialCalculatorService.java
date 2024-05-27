package com.example.recommendation_service.service.impl;

import com.example.recommendation_service.Structures.EarnFee;
import com.example.recommendation_service.Structures.MonteCarloResults;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FinancialCalculatorService {

    private final Random random = new Random();

    public List<EarnFee> futureValue(float principal, float monthlyContribution, float annualReturnRate, float annualManagementFee, int investmentPeriod) {
        annualReturnRate /= 100;
        annualManagementFee /= 100;
        float effectiveAnnualRate = annualReturnRate - annualManagementFee;
        float monthlyEffectiveRate = effectiveAnnualRate / 12;
        float monthlyReturnRate = annualReturnRate / 12;
        int totalMonths = investmentPeriod * 12;

        List<EarnFee> earningsOverTime = new ArrayList<>();
        float fvWithFees = principal;
        float fvNoFees = principal;

        for (int i = 1; i <= totalMonths; i++) {
            fvWithFees = (fvWithFees + monthlyContribution) * (1 + monthlyEffectiveRate);
            fvNoFees = (fvNoFees + monthlyContribution) * (1 + monthlyReturnRate);
            if (i % 12 == 0 || i == totalMonths) {
                float totalManagementFees = fvNoFees - fvWithFees;
                earningsOverTime.add(new EarnFee(fvWithFees, totalManagementFees));
            }
        }

        return earningsOverTime;
    }

    public double calculateMonthlySavings(double goalAmount, double annualRate, int years) {
        double monthlyRate = annualRate / 12;
        int months = years * 12;
        return goalAmount / ((Math.pow(1 + monthlyRate, months) - 1) / monthlyRate);
    }

    public MonteCarloResults monteCarloSimulation(double initialAmount, double meanReturn, double volatility, int years, double inflationRate, int simulations) {
        double[][] yearlyValues = new double[simulations][years + 1]; // Stores yearly portfolio values for each simulation

        for (int i = 0; i < simulations; i++) {
            double portfolioValue = initialAmount;
            double annualWithdrawal = initialAmount * 0.04; // 4% of initial amount
            yearlyValues[i][0] = portfolioValue; // Initial value

            for (int year = 1; year <= years; year++) {
                if (portfolioValue <= 0) {
                    portfolioValue = 0;
                    break;
                }

                // Generate a random return based on mean and volatility
                double yearlyReturn = meanReturn + volatility * random.nextGaussian();
                portfolioValue *= (1 + yearlyReturn);
                portfolioValue -= annualWithdrawal;

                // Adjust annual withdrawal for inflation
                annualWithdrawal *= (1 + inflationRate);

                yearlyValues[i][year] = portfolioValue; // Store value for the current year
            }
        }

        // Calculate the mean final portfolio value
        double totalFinalValue = 0;
        int successfulSimulations = 0;
        double worstCaseScenario = Double.MAX_VALUE;

        for (int i = 0; i < simulations; i++) {
            double finalValue = yearlyValues[i][years];
            totalFinalValue += finalValue;
            if (finalValue > 0) {
                successfulSimulations++;
            }
            if (finalValue < worstCaseScenario) {
                worstCaseScenario = finalValue;
            }
        }

        double meanFinalValue = totalFinalValue / simulations;
        double successRate = (double) successfulSimulations / simulations;

        return new MonteCarloResults(yearlyValues, meanFinalValue, successRate, worstCaseScenario);
    }
}
