package com.example.recommendation_service.dto;

import com.example.recommendation_service.model.Investor;
import com.example.recommendation_service.model.Portfolio;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvestorDTO {

    private long id;
    private List<PortfolioDTO> portfolioList;

    // convert investorDTO to investor
    public Investor convertToModel(InvestorDTO investorDTO) {
        Investor investor = new Investor();
        investor.setId(investorDTO.getId());
        investor.setPortfolioList(investorDTO.convertToModelList(investorDTO.getPortfolioList()));
        return investor;
    }

    // convert to portfolio model list
    public List<Portfolio> convertToModelList(List<PortfolioDTO> portfolioListDTO) {
        List<Portfolio> portfolioList = new ArrayList<>();
        for (PortfolioDTO portfolioDTO : portfolioListDTO) {
            portfolioList.add(portfolioDTO.convertToModel(portfolioDTO));
        }
        return portfolioList;
    }
}
