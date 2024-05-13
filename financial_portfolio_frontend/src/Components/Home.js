import "../CSS/Home.css";

const Home = () => {

  const username = localStorage.getItem("username");  

  return ( 
    <div className="home">
      <div className="container">
        <div id="carouselExampleCaptions" className="carousel slide" data-bs-ride="carousel" data-bs-interval="5000" style={{ width: '50%', margin: 'auto' }}>
          <div className="carousel-indicators">
            <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="0" className="active" aria-current="true" aria-label="Slide 1"></button>
            <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="1" aria-label="Slide 2"></button>
            <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="2" aria-label="Slide 3"></button>
          </div>
          <div className="carousel-inner">
            <div className="carousel-item active">
              <img src="/world_of_stocks.png" className="d-block w-100" alt="..." />
              <div className="carousel-caption d-none d-md-block">
                <h5>Security</h5>
                {/* <p>Some representative placeholder content for the first slide.</p> */}
              </div>
            </div>
            <div className="carousel-item">
              <img src="/bull.png" className="d-block w-100" alt="..." />
              <div className="carousel-caption d-none d-md-block">
                <h5>Performance</h5>
                {/* <p>Some representative placeholder content for the second slide.</p> */}
              </div>
            </div>
            <div className="carousel-item">
              <img src="/graph.png" className="d-block w-100" alt="..." />
              <div className="carousel-caption d-none d-md-block">
                <h5>User Friendly</h5>
                {/* <p>Some representative placeholder content for the third slide.</p> */}
              </div>
            </div>
          </div>
          <button className="carousel-control-prev" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="prev">
            <span className="carousel-control-prev-icon" aria-hidden="true"></span>
            <span className="visually-hidden">Previous</span>
          </button>
          <button className="carousel-control-next" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="next">
            <span className="carousel-control-next-icon" aria-hidden="true"></span>
            <span className="visually-hidden">Next</span>
          </button>
        </div>

        <hr id="features" className="section-divider" /> {/* Add a horizontal line */}
      </div>

      <div className="container content-container">
        
        <div className="features-container" >
          <div className="feature-image">
            <img src="/global.jpeg" alt="Feature Image" />
          </div>
          <div className="feature-text">
            <h2 style={{ marginBottom: '20px' }}>Features</h2>
            <p>
              <ul style={{ textAlign: 'left' }}>
                <li style={{ marginBottom: '10px' }}>
                  This website allows users to 
                  enter and update data
                  from the portfolio. Analyzing
                  portfolio performance and a
                  market trends. Generation of
                  custom reports, charts and personalized recommendations, which can be used
                  to evaluate performance and take
                  investment decisions.
                </li>
                <li style={{ marginBottom: '10px' }}>
                  As a standalone product, "Financial Portfolio Visualizer" offers its users
                  extensive analysis and reporting functionality without the need to integrate with other applications or systems.
                  Its intuitive and easy-to-use interface allows users to access and manage their financial portfolios
                  without the need for previous experience in the financial field.
                </li>
                <li style={{ marginBottom: '10px' }}>Replication of BET index</li>
                <li style={{ marginBottom: '10px' }}>Financial calculator</li>
              </ul>
            </p>
          </div>
        </div>

        <hr id="pricing" className="section-divider" /> {/* Add a horizontal line */}

        <div className="pricing-container">
          <div className="pricing-text">
            <h2>Pricing</h2>
            <p>Some text about the princing...</p>
          </div>
          <div className="pricing-image">
            <img src="/pricing_dark.png" alt="Pricing Image" />
          </div>
        </div>

        <hr id="about" className="section-divider" /> {/* Add a horizontal line */}
        
        <div className="about-container">
          <div className="about-image">
            <img src="/about_us.jpeg" alt="About Image" />
          </div>
          <div className="feature-text">
            <h2>About</h2>
            <p>Some text about us...</p>
          </div>
        </div>
      </div>
      

    </div>
   );
}
 
export default Home;