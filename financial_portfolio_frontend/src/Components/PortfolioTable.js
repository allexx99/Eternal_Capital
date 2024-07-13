import React from 'react';
import axios from "axios";
import { useEffect, useState } from "react";

const PortfolioTable = ({ portfolio }) => {
  const hasStocks = portfolio.stocks && portfolio.stocks.length > 0;

  const [symbolRecommendation, setSymbolRecommendation] = useState({});


  const [totalPartialSpent, setTotalPartialSpent] = useState(0);
  const [fee, setFee] = useState(0);

  useEffect(() => {
    // Retrieve the fee from local storage
    const storedFee = localStorage.getItem(`fee_${portfolio.id}`);
    if (storedFee) {
      setFee(parseFloat(storedFee));
    }
  }, [portfolio.id]);

  useEffect(() => {
    // Retrieve the totalPartialSpent from local storage
    const storedTotalPartialSpent = localStorage.getItem(`totalPartialSpent_${portfolio.id}`);
    if (storedTotalPartialSpent) {
      setTotalPartialSpent(parseFloat(storedTotalPartialSpent));
    }
  }, [portfolio.id]);


  useEffect(() => {
    const savedRecommendations = localStorage.getItem(`recommendations_${portfolio.id}`);
    if (savedRecommendations) {
      setSymbolRecommendation(JSON.parse(savedRecommendations));
    }
  }, [portfolio.id]);

  function balanceStocks() {
    const stocksToBalance = portfolio.stocks.map(stock => {
      return {
        betIndex: {
          id: stock.betIndex.id,
          symbol: stock.betIndex.symbol,
          price: stock.betIndex.price,
          weight: stock.betIndex.weight
        },
        quantity: stock.quantity,
        normWeight: 0
    };
  });

  const investorId = localStorage.getItem("userId");
  const portfolioId = portfolio.id;

  axios.post(`http://localhost:8081/balanceStocks/${investorId}/${portfolioId}?investedSum=5000&tradingFee=0.0043&fixedFee=1.5&minTransaction=500`, stocksToBalance, 
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    }).then((response) => {
      let totalPartialSpent = 0;
      const recommendations = response.data.reduce((acc, rec) => {
        acc[rec.symbol] = rec.recommendation;
        totalPartialSpent += rec.partialSpent;
        return acc;
      }, {});
      
      localStorage.setItem(`recommendations_${portfolioId}`, JSON.stringify(recommendations));
      localStorage.setItem(`totalPartialSpent_${portfolioId}`, totalPartialSpent.toString());
      setSymbolRecommendation(recommendations);

      let fee = 0;
      fee = totalPartialSpent * 0.0043 + response.data.length * 1.5;
      localStorage.setItem(`fee_${portfolioId}`, fee.toString());

      window.location.reload();
    });
  }

  // useEffect(() => {
  //   console.log(symbolRecommendation["SNP"]);
  // },[symbolRecommendation]);

  const formatPercentage = (value) => (value * 100).toFixed(2);

  return (
    <div className="portfolio-table">
      <h5>Portfolio #{portfolio.id} - Total Spent: {totalPartialSpent.toFixed(2)} + fee {fee.toFixed(2)}</h5>
      <table>
        <thead>
          <tr>
            <th>Symbol</th>
            <th>Price</th>
            <th>Weight</th>
            <th>Norm Weight</th>
            <th>Old Quantity</th>
            <th>Recommendation</th>
            <th>Quantity</th>
          </tr>
        </thead>
        {hasStocks && (
          <tbody>
            {portfolio.stocks.map(stock => (
              <tr key={stock.id}>
                <td>{stock.betIndex.symbol}</td>
                <td>{stock.betIndex.price}</td>
                <td>{stock.betIndex.weight}</td>
                <td>{formatPercentage(stock.normWeight)}</td>
                {/* <td>{(stock.quantity - symbolRecommendation[stock.betIndex.symbol]) || 0}</td> */}
                <td>{symbolRecommendation[stock.betIndex.symbol] ? (stock.quantity - symbolRecommendation[stock.betIndex.symbol]) : stock.quantity}</td>
                <td>{symbolRecommendation[stock.betIndex.symbol] || 0}</td>
                <td>{stock.quantity}</td>
              </tr>
            ))}
          </tbody>
        )}
      </table>

      <button onClick={balanceStocks}className="button-table">Balance Stocks</button>
    </div>
  );
};

export default PortfolioTable;
