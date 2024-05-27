import React, { useState, useRef, useEffect } from "react";
import axios from "axios";
import { Line } from "react-chartjs-2";
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';
import "../CSS/MonteCarlo.css";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);


const MonteCarlo = () => {

  const [initialAmount, setInitialAmount] = useState(1000000);
  const [meanReturn, setMeanReturn] = useState(7);
  const [volatility, setVolatility] = useState(12);
  const [years, setYears] = useState(30);
  const [inflationRate, setInflationRate] = useState(2);
  const [simulations, setSimulations] = useState(10000);
  const [data, setData] = useState(null);
  const resultRef = useRef(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    await axios.post("http://localhost:8081/monteCarloSimulation", null, {
      params: {
        initialAmount,
        meanReturn,
        volatility,
        years,
        inflationRate,
        simulations,
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
    <div className="monte-carlo">

      <div className="input-data">
        <form onSubmit={handleSubmit}>
          <label>
            Initial Amount:
            <input type="number" value={initialAmount} onChange={(e) => setInitialAmount(parseFloat(e.target.value))} />
          </label>
          <label>
            Mean Return:
            <input type="number" step="0.01" value={meanReturn} onChange={(e) => setMeanReturn(parseFloat(e.target.value))} />
          </label>
          <label>
            Volatility:
            <input type="number" step="0.01" value={volatility} onChange={(e) => setVolatility(parseFloat(e.target.value))} />
          </label>
          <label>
            Years:
            <input type="number" value={years} onChange={(e) => setYears(parseInt(e.target.value))} />
          </label>
          <label>
            Inflation Rate:
            <input type="number" step="0.01" value={inflationRate} onChange={(e) => setInflationRate(parseFloat(e.target.value))} />
          </label>
          <label>
            Simulations:
            <input type="number" value={simulations} onChange={(e) => setSimulations(parseInt(e.target.value))} />
          </label>
          <button type="submit">Calculate</button>
        </form>
      </div>

      <hr className="scton-devider" ref={resultRef} />

      <div className="result-graph">
        
      {data && <ResultsGraph data={data.yearlyValues} years={years} meanFinalValue={data.meanFinalValue} successRate={data.successRate} worstCaseScenario={data.worstCaseScenario} medianValues={data.medianValue} percentile25Values={data.percentile25Values} percentile75Values={data.percentile75Values} />}
      </div>
    </div>
   );
};

const getRandomSimulations = (data, numSimulations) => {
  const randomIndices = [];
  const randomSimulations = [];
  const dataLength = data.length;

  while (randomIndices.length < numSimulations) {
    const randomIndex = Math.floor(Math.random() * dataLength);
    if (!randomIndices.includes(randomIndex)) {
      randomIndices.push(randomIndex);
      randomSimulations.push(data[randomIndex]);
    }
  }

  return randomSimulations;
};

const ResultsGraph = ({ data, years, meanFinalValue, successRate, worstCaseScenario, medianValues, percentile25Values, percentile75Values }) => {
  const labels = Array.from({ length: years + 1 }, (_, i) => `Year ${i}`);
  const randomSimulations = getRandomSimulations(data, 10);
  const datasets = randomSimulations.map((simulation, index) => ({
    label: `Simulation ${index + 1}`,
    data: simulation,
    borderColor: `rgba(${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, 0.5)`,
    backgroundColor: `rgba(${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, 0.1)`,
    fill: false,
  })).concat([
    {
      label: 'Median',
      data: medianValues,
      borderColor: 'rgba(0, 0, 255, 0.5)',
      backgroundColor: 'rgba(0, 0, 255, 0.1)',
      borderDash: [5, 5],
      fill: false,
    },
    {
      label: '25th Percentile',
      data: percentile25Values,
      borderColor: 'rgba(0, 255, 0, 0.5)',
      backgroundColor: 'rgba(0, 255, 0, 0.1)',
      borderDash: [5, 5],
      fill: false,
    },
    {
      label: '75th Percentile',
      data: percentile75Values,
      borderColor: 'rgba(255, 0, 0, 0.5)',
      backgroundColor: 'rgba(255, 0, 0, 0.1)',
      borderDash: [5, 5],
      fill: false,
    }
  ]);

  const chartData = {
    labels,
    datasets,
  };

  return (
    <div>
      <Line data={chartData} />
      <div className="totals">
        <p>Mean Final Value: ${meanFinalValue.toFixed(2)}</p>
        <p>Success Rate: {(successRate * 100).toFixed(2)}%</p>
        <p>Worst Case Scenario: ${worstCaseScenario.toFixed(2)}</p>
      </div>
    </div>
  );
};
 
export default MonteCarlo;