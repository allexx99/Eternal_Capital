package com.example.recommendation_service.repository;

import com.example.recommendation_service.model.Stock;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepo extends JpaRepository<Stock, Long> {
    @Transactional
    void deleteStocksByPortfolioId(long portfolioId);

}
