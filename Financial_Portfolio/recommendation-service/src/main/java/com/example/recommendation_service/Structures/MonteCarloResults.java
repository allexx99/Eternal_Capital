package com.example.recommendation_service.Structures;

public class MonteCarloResults {

    private final double[][] yearlyValues;
    private final double meanFinalValue;
    private final double successRate;
    private final double worstCaseScenario;
    private final double[] medianValue;
    private final double[] percentile25Values;
    private final double[] percentile75Values;

    public MonteCarloResults(double[][] yearlyValues, double meanFinalValue, double successRate, double worstCaseScenario, double[] medianValue, double[] percentile25Values, double[] percentile75Values) {
        this.yearlyValues = yearlyValues;
        this.meanFinalValue = meanFinalValue;
        this.successRate = successRate;
        this.worstCaseScenario = worstCaseScenario;
        this.medianValue = medianValue;
        this.percentile25Values = percentile25Values;
        this.percentile75Values = percentile75Values;
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

    public double[] getMedianValue() {
        return medianValue;
    }

    public double[] getPercentile25Values() {
        return percentile25Values;
    }

    public double[] getPercentile75Values() {
        return percentile75Values;
    }
}
