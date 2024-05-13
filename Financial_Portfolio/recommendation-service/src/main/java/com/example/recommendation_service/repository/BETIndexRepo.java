package com.example.recommendation_service.repository;

import com.example.recommendation_service.model.BETIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BETIndexRepo extends JpaRepository<BETIndex, Long> {
    BETIndex findById(long id);
    BETIndex findBySymbol(String symbol);
//    BETIndex findStockByCompanyName(String companyName);
    BETIndex findStockByPrice(float price);
    BETIndex findStockByWeight(float weight);
}
