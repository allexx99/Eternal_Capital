package com.example.recommendation_service.service;

import com.example.recommendation_service.dto.BETIndexDTO;
import com.example.recommendation_service.model.BETIndex;

import java.util.List;

public interface BETIndexServiceInterface {
    BETIndex addStock(BETIndexDTO BETIndexDTO);
    BETIndex getStockById(long id);
    List<BETIndex> getStocks();
}
