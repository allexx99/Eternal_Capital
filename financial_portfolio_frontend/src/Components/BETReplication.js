import { useEffect, useState } from "react";
import axios from "axios";
import "../CSS/BETReplication.css";

const BETReplication = () => {

  const [stocks, setStocks] = useState([]);
  const [selectedRows, setSelectedRows] = useState([]);
  const userId = localStorage.getItem("userId");
  const [showModal, setShowModal] = useState(false);
  const [portfolios, setPortfolios] = useState([]);
  const [selectedPortfolio, setSelectedPortfolio] = useState(null);
  const [selectedButtons, setSelectedButtons] = useState([]);

  // useEffect(() => {
  //   axios.get("http://localhost:8081/getPortfolios/" + userId, {
  //     headers: {
  //       Authorization: `Bearer ${localStorage.getItem("token")}`
  //     }
  //   }).then((response) => {
  //     // console.log(response.data);
  //     setPortfolios(response.data);
  //   })
  // }, []);

  // function handleBalanceStocks() {
  //   setShowModal(true);
  // }

  // useEffect(() => {
  //   console.log(showModal);
  // }, [showModal]);

  // useEffect(() => {
  //   axios.get("http://localhost:8081/readStocksOnline", {
  //     headers: {
  //       Authorization: `Bearer ${localStorage.getItem("token")}`
  //     }
  //   })

  //   axios.get("http://localhost:8081/getStocks", {
  //     headers: {
  //       Authorization: `Bearer ${localStorage.getItem("token")}`
  //     }
  //   }).then((response) => {
  //     const sortedStocks = response.data.sort((a, b) => b.weight - a.weight);
  //     setStocks(sortedStocks);
  //   })
  // }, []);

  useEffect(() => {
    axios.get("http://localhost:8081/readStocksOnline", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    }).then(() => {
      // Add a delay of 1 second before fetching stocks
      setTimeout(() => {
        axios.get("http://localhost:8081/getStocks", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`
          }
        }).then((response) => {
          const sortedStocks = response.data.sort((a, b) => b.weight - a.weight);
          setStocks(sortedStocks);
        });
      }, 20);
    });
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


  // Modal
  // Get the modal
  var modal = document.getElementById("myModal");

  // Modal
  function openModal() {
    axios.get("http://localhost:8081/getPortfolios/" + userId, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    }).then((response) => {
      // console.log(response.data);
      setPortfolios(response.data);
    })
    modal.style.display = "block";
  }

  function closeModal() {
    modal.style.display = "none";
    setSelectedPortfolio(null);
    setSelectedButtons([]);
  }

  // When the user clicks anywhere outside of the modal, close it
  window.onclick = function(event) {
    if (event.target === modal) {
      modal.style.display = "none";
    }
  }

  // function handleSelectPortfolio(portfolioId) {
  //   setSelectedPortfolio(portfolioId);
  // }

  useEffect(() => {
    console.log(selectedPortfolio);
  }, [selectedPortfolio]);

  const toggleButtonSelection = (portfolioId) => {
    setSelectedPortfolio(portfolioId);
    setSelectedButtons(prevSelectedButtons => {
      // Check if the clicked button is already selected
      const alreadySelected = prevSelectedButtons.includes(portfolioId);
  
      // If already selected, deselect it
      if (alreadySelected) {
        setSelectedPortfolio(null);
        return [];
      } else {
        // Deselect the previously selected button (if any) and select the new one
        return [portfolioId];
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
      <button onClick={openModal} className="btn btn-primary">Balance Stocks</button>
    </div>

    <div id="myModal" className="modal">

      <div className="modal-content">
        <button onClick={closeModal} className="close">&times;</button>
        {/* <p>Some text in the Modal..</p> */}
        <h4>My Portfolios</h4>
        <div className="portfolio-buttons">
          {portfolios.map(portfolio => (
            <button 
              key={portfolio.id}
              className={selectedButtons.includes(portfolio.id) ? "selected-portf" : ""}
              onClick={() => toggleButtonSelection(portfolio.id)}
            >
              #{portfolio.id}
              </button>
          ))}
        </div>

        <div className="balance-button">
          <button>Balance Stocks</button>
        </div>
        
      </div>

    </div>

  </div>
   );
}
 
export default BETReplication;