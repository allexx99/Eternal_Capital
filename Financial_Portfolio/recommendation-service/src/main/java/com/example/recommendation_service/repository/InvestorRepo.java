package com.example.recommendation_service.repository;

import com.example.recommendation_service.model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestorRepo extends JpaRepository<Investor, Long> {
    Investor findInvestorById(long id);
}
