import { useEffect, useState } from "react";
import PortfolioTable from "./PortfolioTable";  // Import the new component
import axios from "axios";
import "../CSS/MyPortfolios.css";

const MyPortfolios = () => {

  const [portfolios, setPortfolios] = useState([]);
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    axios.get("http://localhost:8081/getPortfolios/" + userId, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    }).then((response) => {
      // console.log(response.data);
      setPortfolios(response.data);
    })
  }, []);

  useEffect(() => {
    console.log(portfolios);
  }, [portfolios]);
  
  return ( 
    <div className="my-portfolios">
      <div className="portfolio-title">
        <h2>
          My Portfolios
        </h2>
      </div>
      <div className="additional-info">
        <p>
          Here are your portfolios. <br />
          The computation of the recommendations is based on the following information:
        </p>
          <ul>
            <li>The total amount you want to invest: 5000 ron</li>
            <li>Trading fee: 0.43%</li>
            <li>Fixed fee: 1.5 ron</li>
            <li>Minimum transaction cost: 500 ron</li>
          </ul>
      </div>
      <div className="portfolios-wrapper">
        {portfolios.map(portfolio => (
            <PortfolioTable key={portfolio.id} portfolio={portfolio} />
        ))}

      </div>  
    </div>
   );
}
 
export default MyPortfolios;