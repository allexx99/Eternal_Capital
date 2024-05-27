package com.example.recommendation_service.Structures;

public class MonteCarloResults {

    private final double[][] yearlyValues;
    private final double meanFinalValue;
    private final double successRate;
    private final double worstCaseScenario;

    public MonteCarloResults(double[][] yearlyValues, double meanFinalValue, double successRate, double worstCaseScenario) {
        this.yearlyValues = yearlyValues;
        this.meanFinalValue = meanFinalValue;
        this.successRate = successRate;
        this.worstCaseScenario = worstCaseScenario;
    }

    public double[][] getYearlyValues() {
        return yearlyValues;
    }

    public double getMeanFinalValue() {
        return meanFinalValue;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public double getWorstCaseScenario() {
        return worstCaseScenario;
    }
}
