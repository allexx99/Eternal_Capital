import React, { useState, useRef, useEffect } from "react";
import "../CSS/FutureValue.css";
import axios from "axios";
import { Line } from "react-chartjs-2";
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

const FutureValue = () => {
  const [principal, setPrincipal] = useState(0);
  const [monthlyContribution, setMonthlyContribution] = useState(0);
  const [annualReturnRate, setAnnualReturnRate] = useState(0);
  const [annualManagementFee, setAnnualManagementFee] = useState(0);
  const [investmentPeriod, setInvestmentPeriod] = useState(0);
  const [data, setData] = useState(null);
  const resultRef = useRef(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    await axios.post("http://localhost:8081/futureValue", null, {
      params: {
        principal,
        monthlyContribution,
        annualReturnRate,
        annualManagementFee,
        investmentPeriod,
      },
    },
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    }).then((response) => {
      setData(response.data);
    });
  };

  useEffect(() => {
    if (data) {
      resultRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [data]);

  return (
    <div className="future-value">
      <div className="input-data">
        <form onSubmit={handleSubmit}>
          <label>
            Principal (ron):
            <input type="number" value={principal} onChange={(e) => setPrincipal(parseFloat(e.target.value))} />
          </label>
          <label>
            Monthly Contribution (ron):
            <input type="number" value={monthlyContribution} onChange={(e) => setMonthlyContribution(parseFloat(e.target.value))} />
          </label>
          <label>
            Annual Return Rate (%):
            <input type="number" step="0.01" value={annualReturnRate} onChange={(e) => setAnnualReturnRate(parseFloat(e.target.value))} />
          </label>
          <label>
            Annual Management Fee (%):
            <input type="number" step="0.01" value={annualManagementFee} onChange={(e) => setAnnualManagementFee(parseFloat(e.target.value))} />
          </label>
          <label>
            Investment Period (years):
            <input type="number" value={investmentPeriod} onChange={(e) => setInvestmentPeriod(parseInt(e.target.value))} />
          </label>
          <button type="submit">Calculate</button>
        </form>
        
      </div>

      <hr className="section-devider" ref={resultRef}/>

      <div className="result-graph">
        {data && <ResultsGraph data={data} />}
      </div>
    </div>
  );
};

const ResultsGraph = ({ data }) => {
  const labels = data.map((_, index) => `Year ${index + 1}`);
  const earnings = data.map((item) => item.earn);
  const fees = data.map((item) => item.fee);

  const chartData = {
    labels,
    datasets: [
      {
        label: 'Earnings',
        data: earnings,
        borderColor: 'rgba(75,192,192,1)',
        backgroundColor: 'rgba(75,192,192,0.2)',
      },
      {
        label: 'Fees',
        data: fees,
        borderColor: 'rgba(255,99,132,1)',
        backgroundColor: 'rgba(255,99,132,0.2)',
      },
    ],
  };

  const totalEarnings = earnings[earnings.length - 1];
  const totalFees = fees[fees.length - 1];

  return (
    <div>
      <Line data={chartData} />
      <div className="totals">
        <p>Total Earnings: {totalEarnings.toFixed(2)} ron</p>
        <p>Total Fees: {totalFees.toFixed(2)} ron</p>
      </div>
    </div>
  );
};

export default FutureValue;
