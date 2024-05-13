import { useEffect, useState } from "react";
import "../CSS/MyPortfolios.css";
import axios from "axios";

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
  
  return ( 
    <div className="my-portfolios">
      <h1>My Portfolios</h1>
      <ul>
        {portfolios.map(portfolio => (
          <li key={portfolio.id}>#{portfolio.id}</li>
        ))}
      </ul>
    </div>
   );
}
 
export default MyPortfolios;