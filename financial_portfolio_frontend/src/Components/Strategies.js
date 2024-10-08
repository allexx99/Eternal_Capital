import { useNavigate } from "react-router-dom";
import "../CSS/Strategies.css";

const Strategies = () => {

  const navigate = useNavigate("");

  const navigateTo = (path) => {
    navigate(path);
    // window.location.reload();
  }

  return ( 
    <div className="strategies">
      <div className="body-content">
        
        <div className="card" style={{ width: "18rem" }} onClick={() => navigateTo("/strategies/bet-replication")}>
          <img src="/chart_up.jpg" className="card-img-top" alt="..." />
          <div className="card-body">
            <p className="card-text" style={{ fontWeight: 'bold' }}>BET Index Replication</p>
            <p>Generate personalized recommendations for your portfolios</p>
          </div>
        </div>

        <div className="card" style={{ width: "18rem" }} onClick={() => navigateTo("/strategies/financial-calculator")}>
          <img src="/about_us.jpeg" className="card-img-top" alt="..." />
          <div className="card-body">
            <p className="card-text" style={{ fontWeight: 'bold' }}>Financial Calculators</p>
            <p>Maximize your earnings using financial calculators</p>
          </div>
        </div>

      </div>
      
    </div>
   );
}
 
export default Strategies;