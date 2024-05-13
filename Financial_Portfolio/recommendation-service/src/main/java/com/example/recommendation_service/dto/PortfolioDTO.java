package com.example.recommendation_service.dto;

import com.example.recommendation_service.model.Portfolio;
import com.example.recommendation_service.model.Stock;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PortfolioDTO {

        private long id;
        private List<StockDTO> stockListDTO;

        // convert portfolioDTO to portfolio
        public Portfolio convertToModel(PortfolioDTO portfolioDTO) {
            Portfolio portfolio = new Portfolio();
            portfolio.setId(portfolioDTO.getId());
            portfolio.setStocks(portfolioDTO.convertToModelList(portfolioDTO.getStockListDTO()));
            return portfolio;
        }

        public List<Stock> convertToModelList(List<StockDTO> stockListDTO) {
            List<Stock> stockList = new ArrayList<>();
            if (stockListDTO == null) {
                return new ArrayList<>();
            } else {
                for (StockDTO stockDTO : stockListDTO) {
                    stockList.add(stockDTO.convertToModel(stockDTO));
                }
                return stockList;
            }
        }
}
