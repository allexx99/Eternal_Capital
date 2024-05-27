import "../CSS/FinancialCalculator.css"

const FinancialCalculator = () => {
  return ( 
    <div className="financial-calculator">
      <div className="card">
        <h5 className="card-header">Featured</h5>
        <div className="card-body">
          <h5 className="card-title">Future value</h5>
          <p className="card-text">Determine the value of an investment or asset at a future point in time.</p>
          <a href="/strategies/financial-calculator/future-value" className="btn btn-primary">Simulation</a>
        </div>
      </div>

      <div className="card">
        <h5 className="card-header">Featured</h5>
        <div className="card-body">
          <h5 className="card-title">Monthly savings</h5>
          <p className="card-text">With supporting text below as a natural lead-in to additional content.</p>
          <a href="#" className="btn btn-primary">Simulation</a>
        </div>
      </div>

      <div className="card">
        <h5 className="card-header">Featured</h5>
        <div className="card-body">
          <h5 className="card-title">Monte Carlo</h5>
          <p className="card-text">With supporting text below as a natural lead-in to additional content.</p>
          <a href="#" className="btn btn-primary">Simulation</a>
        </div>
      </div>
        
    </div>

   );
}
 
export default FinancialCalculator;