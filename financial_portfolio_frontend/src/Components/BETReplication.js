import { useEffect, useState } from "react";
import axios from "axios";
import "../CSS/BETReplication.css";

const BETReplication = () => {

  const [stocks, setStocks] = useState([]);
  const [selectedRows, setSelectedRows] = useState([]);
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    axios.get("http://localhost:8081/readStocksOnline", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    })

    axios.get("http://localhost:8081/getStocks", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    }).then((response) => {
      const sortedStocks = response.data.sort((a, b) => b.weight - a.weight);
      setStocks(sortedStocks);
    })
  }, []);

  useEffect(() => {
    console.log(selectedRows);
  }, [selectedRows]);

  const toggleRowSelection = (symbol) => {
    setSelectedRows(prevSelectedRows => {
      if (prevSelectedRows.includes(symbol)) {
        return prevSelectedRows.filter(item => item !== symbol);
      } else {
        return [...prevSelectedRows, symbol];
      }
    });
  };
  
  function handleCreatePortfolio() {
    axios.post("http://localhost:8081/addPortfolio/" + userId, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    });
  };

  return ( 
    <div className="bet-replication">
      <div className="stocks-table">
        <table>
        <thead>
          <tr>
            <th>Symbol</th>
            <th>Price</th>
            <th>Weight</th>
          </tr>
        </thead>
        <tbody>
          {stocks.map((stock, index) => (
            <tr 
              key={index}
              onClick={() => toggleRowSelection(stock.symbol)}
              className={selectedRows.includes(stock.symbol) ? "selected" : ""}
            >
              <td>{stock.symbol}</td>
              <td>{stock.price}</td>
              <td>{stock.weight}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
    
    <div className="buttons-table">
      <button onClick={handleCreatePortfolio} className="btn btn-primary">Create Portfolio</button>
      <button className="btn btn-primary">Balance Stocks</button>
    </div>

  </div>
   );
}
 
export default BETReplication;