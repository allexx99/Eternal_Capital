import { useEffect, useState } from "react";
import "../CSS/Admin.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrashAlt } from "@fortawesome/free-solid-svg-icons";

const Admin = () => {

  const [users, setUsers] = useState([]);
  const navigate = useNavigate("");

  useEffect(() => {
    axios.get("http://localhost:8080/getUsers", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    }).then(response => {
      setUsers(response.data);
    });
  }, []);

  function handleDelete(id) {
    axios.delete(`http://localhost:8080/deleteUser/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    }).then(response => {
      window.location.reload();
    });
  }

  return ( 
    <div className="admin">
      {users.map(user => (
        <div className="card" key={user.id}>
          <h5 className="card-header">{user.username}</h5>
          <div className="card-body">
            <h5 className="card-title">ID: {user.id}</h5>
            <p className="card-text">Name: {user.firstName}</p>
          </div>
          <button onClick={() => handleDelete(user.id)}>
            <FontAwesomeIcon icon={faTrashAlt} />
          </button>
        </div>
      ))}

    </div>
   );
}
 
export default Admin;