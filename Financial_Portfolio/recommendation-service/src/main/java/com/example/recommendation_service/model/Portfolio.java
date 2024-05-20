package com.example.recommendation_service.model;

import com.example.recommendation_service.dto.StockDTO;
import jakarta.persistence.*;
import lombok.*;

import com.example.recommendation_service.dto.PortfolioDTO;

import java.util.ArrayList;
import java.util.List;

//@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "portfolio")
public class Portfolio {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "portfolio")
    private List<Stock> stocks;

    // convert portfolio to portfolioDTO
    public PortfolioDTO convertToDTO(Portfolio portfolio) {
        PortfolioDTO portfolioDTO = new PortfolioDTO();
        portfolioDTO.setId(portfolio.getId());
        portfolioDTO.setStockListDTO(portfolio.convertToDTOList(portfolio.getStocks()));
        return portfolioDTO;
    }

    public List<StockDTO> convertToDTOList(List<Stock> stocks) {
        List<StockDTO> stockDTOList = new ArrayList<>();
        for (Stock stock : stocks) {
            stockDTOList.add(stock.convertToDTO(stock));
        }
        return stockDTOList;
    }

}
