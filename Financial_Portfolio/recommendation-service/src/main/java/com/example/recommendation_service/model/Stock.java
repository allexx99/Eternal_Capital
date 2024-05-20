package com.example.recommendation_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.example.recommendation_service.dto.StockDTO;

//@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private BETIndex betIndex;

    @Column
    @NotNull
    private int quantity;

    @Column
    @NotNull
    private float normWeight;

    @ManyToOne
    @JsonIgnore
    private Portfolio portfolio;

    // convert stock to stockDTO
    public StockDTO convertToDTO(Stock stock) {
        StockDTO stockDTO = new StockDTO();
        stockDTO.setId(stock.getId());
        stockDTO.setBetIndex(stock.getBetIndex().convertToDTO(stock.getBetIndex()));
        stockDTO.setQuantity(stock.getQuantity());
        return stockDTO;
    }


}
