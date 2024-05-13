package com.example.recommendation_service.model;

import com.example.recommendation_service.dto.InvestorDTO;
import com.example.recommendation_service.dto.PortfolioDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Investor {

    @Id
    private long id;

    @OneToMany(targetEntity = Portfolio.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "idInvestor", referencedColumnName = "id")
    @NotNull
    private List<Portfolio> portfolioList;

    //convert investor to investorDTO
    public InvestorDTO convertToDTO(Investor investor) {
        InvestorDTO investorDTO = new InvestorDTO();
        investorDTO.setId(investor.getId());
        investorDTO.setPortfolioList(investor.convertToDTOList(investor.getPortfolioList()));
        return investorDTO;
    }

    //convert to portfolioDTO list
    public List<PortfolioDTO> convertToDTOList(List<Portfolio> portfolioList) {
        List<PortfolioDTO> portfolioDTOList = new ArrayList<>();
        for (Portfolio portfolio : portfolioList) {
            portfolioDTOList.add(portfolio.convertToDTO(portfolio));
        }
        return portfolioDTOList;
    }
}
