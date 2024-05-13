package com.example.recommendation_service.repository;

import com.example.recommendation_service.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface PortfolioRepo extends JpaRepository<Portfolio, Long>{
    Portfolio findPortfolioById(long id);
}
