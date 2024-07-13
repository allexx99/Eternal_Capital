package com.example.recommendation_service.service.impl;

import com.example.recommendation_service.dto.InvestorDTO;
import com.example.recommendation_service.dto.PortfolioDTO;
import com.example.recommendation_service.model.Investor;
import com.example.recommendation_service.model.Portfolio;
import com.example.recommendation_service.model.Stock;
import com.example.recommendation_service.repository.InvestorRepo;
import com.example.recommendation_service.repository.PortfolioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvestorService {

    private final InvestorRepo investorRepo;
    private final PortfolioRepo portfolioRepo;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public InvestorService(InvestorRepo investorRepo, PortfolioRepo portfolioRepo, SimpMessagingTemplate messagingTemplate) {
        this.investorRepo = investorRepo;
        this.portfolioRepo = portfolioRepo;
        this.messagingTemplate = messagingTemplate;
    }

    public void addInvestor(long id) {
        InvestorDTO investorDTO = new InvestorDTO();
        List<PortfolioDTO> portfolioListDTO = new ArrayList<>();
        investorDTO.setId(id);
        investorDTO.setPortfolioList(portfolioListDTO);
        Investor investor = investorDTO.convertToModel(investorDTO);
        investorRepo.save(investor);
        publishAddNotification("Investor with ID " + id + " has successfully registered!");
    }

    public List<Investor> getInvestors() {
        return investorRepo.findAll();
    }

    public void deleteInvestor(long id) {
        Investor investor = investorRepo.findInvestorById(id);
        investorRepo.delete(investor);
        publishDeleteNotification("Investor with ID " + id + " has been successfully deleted!");
    }

    public void addPortfolio(long investorId) {
        Investor investor = investorRepo.findInvestorById(investorId);
        PortfolioDTO portfolioDTO = new PortfolioDTO();
        Portfolio portfolio = portfolioDTO.convertToModel(portfolioDTO);
        investor.getPortfolioList().add(portfolio);
        investorRepo.save(investor);
        publishPortfolioNotification("A new empty portfolio has been created!");
    }

    public List<Portfolio> getPortfolios(long investorId) {
        Investor investor = investorRepo.findInvestorById(investorId);
        return investor.getPortfolioList();
    }

    public void addStocksToPortfolio(long investorId, long portfolioId, List<Stock> stocks) {
        Investor investor = investorRepo.findInvestorById(investorId);
        Portfolio portfolio = portfolioRepo.findPortfolioById(portfolioId);

        /*// Set the portfolio for each stock
        for (Stock stock : stocks) {
            stock.setPortfolio(portfolio);
        }

        portfolio.getStocks().addAll(stocks);*/

        // Iterate over the provided list of stocks
        for (Stock stock : stocks) {
            Stock newStock = new Stock();
            newStock.setBetIndex(stock.getBetIndex());
            newStock.setQuantity(stock.getQuantity()); // Retain quantity information
            newStock.setNormWeight(stock.getNormWeight()); // Retain normWeight information
            newStock.setPortfolio(portfolio);
            portfolio.getStocks().add(newStock);
        }

        investorRepo.save(investor);
    }

    public void deletePortfolio(long investorId, long portfolioId) {
        Investor investor = investorRepo.findInvestorById(investorId);
        Portfolio portfolio = portfolioRepo.findPortfolioById(portfolioId);
        portfolioRepo.delete(portfolio);
    }

    private void publishPortfolioNotification(String message) {
        String destination = "/topic/notification" ;
        messagingTemplate.convertAndSend(destination, message);
    }

    private void publishDeleteNotification(String message) {
        String destination = "/topic/notificationDelete" ;
        messagingTemplate.convertAndSend(destination, message);
    }

    private void publishAddNotification(String message) {
        String destination = "/topic/notificationAdd" ;
        messagingTemplate.convertAndSend(destination, message);
    }
}
