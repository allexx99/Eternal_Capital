package com.example.recommendation_service.dto;

import com.example.recommendation_service.model.Stock;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StockDTO {

    private long id;
    private BETIndexDTO betIndex;
    private int quantity;

    // convert stockDTO to stock
    public Stock convertToModel(StockDTO stockDTO) {
        Stock stock = new Stock();
        stock.setId(stockDTO.getId());
        stock.setBetIndex(stockDTO.getBetIndex().convertToModel(stockDTO.getBetIndex()));
        stock.setQuantity(stockDTO.getQuantity());
        return stock;
    }


}
