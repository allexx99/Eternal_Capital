import { useEffect, useState } from "react";
import "../CSS/Admin.css";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrashAlt } from "@fortawesome/free-solid-svg-icons";

import { Client } from '@stomp/stompjs';
import { ToastContainer, toast } from 'react-toastify';

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

  useEffect(() => {
    const client = new Client({
      brokerURL: 'ws://localhost:8081/ws',
      onConnect: () => {
        console.log('Connected to server');
        client.subscribe('/topic/notificationDelete', (message) => {
          if (message.body) {
            console.log("Received notification:", message.body);
            // alert(message.body);
            toast.info(message.body, {
              position: "top-right",
              autoClose: 2000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
          }
        });

        client.subscribe('/topic/notificationAdd', (message) => {
          if (message.body) {
            console.log("Received notification:", message.body);
            // alert(message.body);
            toast.info(message.body, {
              position: "top-right",
              autoClose: 2000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
            setTimeout(() => {
              window.location.reload();
            }, 2000);
          }
        });
      },
      onDisconnect: () => {
        console.log('Disconnected from server');
      }
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  function handleDelete(id) {
    axios.delete(`http://localhost:8080/deleteUser/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    }).then(response => {
      setTimeout(() => {
        window.location.reload();
      }, 2000); // 5000 milliseconds = 5 seconds
    }).catch(error => {
      console.error("There was an error deleting the user!", error);
    });
  }

  const navigateTo = (path) => {
    navigate(path);
    // window.location.reload();
  }

  return ( 
    <div className="admin">
      {users.map(user => (
        <div className="card" key={user.id}>
          <h5 className="card-header">{user.username}</h5>
          <div className="card-body">
            <h5 className="card-title">ID: {user.id}</h5>
            <p className="card-text">Name: {user.firstName}</p>
            {/* <Link to={`/admin/admintouser/${user.id}`}>Edit</Link> */}
            <button className="edit-button" onClick={() => navigateTo(`/admin/admintouser/${user.id}`)}>Edit</button>
          </div>
          <button onClick={() => handleDelete(user.id)}>
            <FontAwesomeIcon icon={faTrashAlt} />
          </button>
        </div>
      ))}

      <ToastContainer />

    </div>
   );
}
 
export default Admin;