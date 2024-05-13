import React from "react";

const Video = () => {
  return ( 
    <video autoPlay loop muted className="video">
      <source src="/money_falling.mp4" type="video/mp4" />
      Your browser does not support the video tag.
    </video>
   );
}
 
export default Video;