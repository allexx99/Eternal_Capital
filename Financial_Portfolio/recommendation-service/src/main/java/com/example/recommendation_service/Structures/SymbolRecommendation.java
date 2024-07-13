package com.example.recommendation_service.Structures;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SymbolRecommendation {

    String symbol;
    float recommendation;
    long portfolioId;
    float partialSpent;

    public SymbolRecommendation(String symbol, float recommendation, long portfolioId, float partialSpent) {
        this.symbol = symbol;
        this.recommendation = recommendation;
        this.portfolioId = portfolioId;
        this.partialSpent = partialSpent;
    }
}
