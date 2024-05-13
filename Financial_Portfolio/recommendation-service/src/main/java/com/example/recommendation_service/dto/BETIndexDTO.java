package com.example.recommendation_service.dto;

import com.example.recommendation_service.model.BETIndex;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BETIndexDTO {

    @JsonProperty("id")
    private long id;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("price")
    private float price;
    @JsonProperty("weight")
    private float weight;

    //convert BETIndexDTO to BETIndex
    public BETIndex convertToModel(BETIndexDTO betIndexDTO) {
        BETIndex betIndex = new BETIndex();
        betIndex.setId(betIndexDTO.getId());
        betIndex.setSymbol(betIndexDTO.getSymbol());
        betIndex.setPrice(betIndexDTO.getPrice());
        betIndex.setWeight(betIndexDTO.getWeight());
        return betIndex;
    }

}
