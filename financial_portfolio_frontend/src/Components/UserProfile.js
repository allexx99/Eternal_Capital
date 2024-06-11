import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "../CSS/UserProfile.css";

const UserProfile = () => {

  // const username = localStorage.getItem("username");
  const id = localStorage.getItem("userId");
  const [username, setUsername] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate("");

  const oldUsername = localStorage.getItem("username");

  useEffect(() => {
    axios.get("http://localhost:8080/getUser?username=" + localStorage.getItem("username"), {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    }).then((response) => {
      setFirstName(response.data.firstName);
      setLastName(response.data.lastName);
      setUsername(response.data.username);
      setEmail(response.data.email);
      setRole(response.data.role);
      setPassword(response.data.password);
    });
}, [])

  function handleModifyData() {

    var usrnm = "";
    if(username === oldUsername) {
      usrnm = oldUsername;
    } else {
      usrnm = username;
    }

    axios.put("http://localhost:8080/updateUser/" + id, {
      username: usrnm,
      password: password,
      firstName: firstName,
      lastName: lastName,
      email: email,
      role: role
    }, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    }).then((response) => {
      console.log(response);
      if(oldUsername !== username) {
        localStorage.setItem("username", '');
        localStorage.setItem("role", '');
        localStorage.setItem("token", '');
        localStorage.setItem("userId", '');
        // Other logout actions
        navigate("/");
        window.location.reload();
      } else {
        alert("Data updated successfully!");
        window.location.reload();
      }
    });
    
  }

  return ( 
    <div className="user-profile">
      <div className="profile-card">
        <div className="profile-header">
          <div className="profile-picture-section">
            <img src="profile-pic-url" alt="Profile" className="profile-picture" />
            <button className="edit-picture">Change picture</button>
          </div>
          <div className="profile-details-section">
            <h2>My Profile</h2>
            <div className="profile-field">
              <label>First Name</label>
              <input 
                type="text" 
                value={firstName}
                onChange={(event) => {setFirstName(event.target.value)}}
                />
            </div>
            <div className="profile-field">
              <label>Last Name</label>
              <input 
              type="text" 
              value={lastName} 
              onChange={(event) => {setLastName(event.target.value)}}
              />
            </div>
            <div className="profile-field">
              <label>Username</label>
              <input 
              type="text" 
              value={username}
              onChange={(event) => {setUsername(event.target.value)}}
              />
            </div>
            <div className="profile-field">
              <label>Email</label>
              <input 
              type="text" 
              value={email} 
              onChange={(event) => {setEmail(event.target.value)}}
              />
            </div>
            <div className="profile-field">
              <label>Role</label>
              <input type="text" value={role} readOnly />
            </div>
            <div className="profile-footer">
              <button onClick={handleModifyData} className="save-button">Save</button>
            </div>
          </div>
        </div>
      </div>
    </div>
   );
}
 
export default UserProfile;