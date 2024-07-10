import React, { useState } from "react";
import axios from "axios";
import "../CSS/MonthlySavings.css";

const MonthlySavings = () => {

  const [goalAmount, setGoalAmount] = useState(0); // Example goal
  const [annualRate, setAnnualRate] = useState(0); // Example return rate
  const [annualManagementFee, setAnnualManagementFee] = useState(0); // Example management fee
  const [years, setYears] = useState(0); // Example period
  const [monthlySavings, setMonthlySavings] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    await axios.post("http://localhost:8081/calculateMonthlySavings", null, {
      params: {
        goalAmount,
        annualRate,
        annualManagementFee,
        years,
      },
    },
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    }).then((response) => {
      setMonthlySavings(response.data);
    });
  };

  return ( 
    <div className="monthly-savings">

      <div className="input-data">
        <form onSubmit={handleSubmit}>
          <label>
            Goal Amount (ron):
            <input type="number" value={goalAmount} onChange={(e) => setGoalAmount(parseFloat(e.target.value))} />
          </label>
          <label>
            Annual Return Rate (%):
            <input type="number" step="0.01" value={annualRate} onChange={(e) => setAnnualRate(parseFloat(e.target.value))} />
          </label>
          <label>
            Annual Management Fee (%):
            <input type="number" step="0.01" value={annualManagementFee} onChange={(e) => setAnnualManagementFee(parseFloat(e.target.value))} />
          </label>
          <label>
            Investment Period (years):
            <input type="number" value={years} onChange={(e) => setYears(parseInt(e.target.value))} />
          </label>
          <button type="submit">Calculate</button>
        </form>
      </div>
      <div className="results">
        <div className="title">
          <h3>Monthly Savings</h3>
        </div>
        {monthlySavings !== null && (
          <div>
            <h5>Monthly Savings Required: ${monthlySavings.toFixed(2)}</h5>
          </div>
        )}
      </div>

    </div>
   );
}
 
export default MonthlySavings;