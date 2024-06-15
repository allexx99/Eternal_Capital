package com.example.recommendation_service.Structures;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SymbolRecommendation {

    String symbol;
    float recommendation;
    long portfolioId;

    public SymbolRecommendation(String symbol, float recommendation, long portfolioId) {
        this.symbol = symbol;
        this.recommendation = recommendation;
        this.portfolioId = portfolioId;
    }
}
