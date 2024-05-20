package com.example.recommendation_service.model;

import com.example.recommendation_service.dto.BETIndexDTO;
import com.example.recommendation_service.dto.BETIndexDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.NotNull;

import java.util.List;

//@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "betindex")
public class BETIndex {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @NotNull
    @Column
    private String symbol;

    @Getter
    @NotNull
    @Column
    private float price;

    @Getter
    @NotNull
    @Column
    private float weight;

    // convert BETIndex to BETIndexDTO
    public BETIndexDTO convertToDTO(BETIndex betIndex) {
        BETIndexDTO betIndexDTO = new BETIndexDTO();
        betIndexDTO.setId(betIndex.getId());
        betIndexDTO.setSymbol(betIndex.getSymbol());
        betIndexDTO.setPrice(betIndex.getPrice());
        betIndexDTO.setWeight(betIndex.getWeight());
        return betIndexDTO;
    }
}
