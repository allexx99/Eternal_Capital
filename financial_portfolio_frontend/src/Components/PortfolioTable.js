import React from 'react';
import axios from "axios";

const PortfolioTable = ({ portfolio }) => {
  const hasStocks = portfolio.stocks && portfolio.stocks.length > 0;

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
    }).then(() => {
      window.location.reload();
    });
  }

  return (
    <div className="portfolio-table">
      <h5>Portfolio #{portfolio.id}</h5>
      <table>
        <thead>
          <tr>
            <th>Symbol</th>
            <th>Price</th>
            <th>Weight</th>
            <th>Norm Weight</th>
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
                <td>{stock.normWeight}</td>
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
