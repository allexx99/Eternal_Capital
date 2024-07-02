import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "../CSS/UserProfile.css";

// CHAT
// import Stomp from "stompjs";
import SockJS from "sockjs-client";
import { over } from "stompjs";

var stompClientChat = null;
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

  /////////////////////////////////////////// CHAT ///////////////////////////////////////////

  const currentUser = localStorage.getItem("username");
  const [privateChats, setPrivateChats] = useState(new Map());
  const [publicChats, setPublicChats] = useState([]);
  // const [users, setUsers] = useState([]);
  const [tab, setTab] = useState("CHATROOM");
  const [userData, setUserData] = useState({
    username: "",
    receivername: "",
    connected: false,
    message: "",
  });
  // State to control chat visibility
  const [showChat, setShowChat] = useState(false);

  useEffect(() => {
    // This will run when the component unmounts
    return () => {
      if (stompClientChat && stompClientChat.connected) {
        stompClientChat.disconnect();
        console.log("Disconnected!");
      }
    };
  }, []);

  // useEffect(() => {
  //   // Fetch the posts from an API or any data source
  //   axios
  //     .get("http://localhost:8080/readUsers")
  //     .then((response) => {
  //       setUsers(response.data);
  //       // console.log(response.data);
  //       // setPosts(response.data);
  //       // setFilteredPosts(response.data);
  //     })
  //     .catch((error) => {
  //       console.log(error);
  //     });
  // }, []);

  // useEffect(() => {
  //   if (stompClientChat && stompClientChat.connected) {
  //     console.log("Already connected.");
  //     return;
  //   }

  //   let Sock = new SockJS("http://localhost:8083/ws");
  //   stompClientChat = over(Sock);
  //   stompClientChat.connect({}, onConnected, onError);
  // }, []);

  // useEffect to run once when the component mounts
  useEffect(() => {
    // Retrieve username from localStorage
    const storedUsername = localStorage.getItem("username");

    // If a username is found in localStorage, update userData
    if (storedUsername) {
      setUserData((prevUserData) => ({
        ...prevUserData,
        username: storedUsername,
      }));
    }
  }, []); // Empty dependency array ensures it runs only once on mount

  const connect = () => {
    if (stompClientChat && stompClientChat.connected) {
      console.log("Already connected.");
      return;
    }
    // setUserData({ ...userData, username: currentUser });
    console.log("sender: " + userData.username);
    console.log("sender: " + currentUser);
    let Sock = new SockJS("http://localhost:8082/ws");
    stompClientChat = over(Sock);
    stompClientChat.connect({}, onConnected, onError);
  };

  const onConnected = () => {
    setUserData({ ...userData, connected: true });
    stompClientChat.subscribe("/chatroom/public", onMessageReceived);
    stompClientChat.subscribe(
      "/user/" + userData.username + "/private",
      onPrivateMessage
    );
    // Show the chat interface
    setShowChat(true);
    userJoin();
  };

  const onError = (err) => {
    console.log(err);
  };

  const userJoin = () => {
    var chatMessage = {
      senderName: userData.username,
      status: "JOIN",
    };

    stompClientChat.send("/app/message", {}, JSON.stringify(chatMessage));
  };
  // const userJoin = () => {
  //   if (stompClientChat && stompClientChat.connected) {
  //     var chatMessage = {
  //       senderName: currentUser,
  //       status: "JOIN",
  //     };
  //     stompClientChat.send("/app/message", {}, JSON.stringify(chatMessage));
  //   } else {
  //     console.log("WebSocket connection is not established.");
  //   }
  // };

  const onMessageReceived = (payload) => {
    var payloadData = JSON.parse(payload.body);
    switch (payloadData.status) {
      case "JOIN":
        if (!privateChats.get(payloadData.senderName)) {
          privateChats.set(payloadData.senderName, []);
          setPrivateChats(new Map(privateChats));
        }
        break;
      case "MESSAGE":
        publicChats.push(payloadData);
        setPublicChats([...publicChats]);
        break;
    }
  };

  // const onPrivateMessage = (payload) => {
  //   console.log(payload);
  //   var payloadData = JSON.parse(payload.body);

  //   if (payloadData.status === "TYPING" && payloadData.senderName === tab) {
  //     setIsTyping(true);
  //     setTimeout(() => setIsTyping(false), 3000);
  //   } else if (payloadData.status !== "TYPING") {
  //     if (privateChats.get(payloadData.senderName)) {
  //       privateChats.get(payloadData.senderName).push(payloadData);
  //       setPrivateChats(new Map(privateChats));
  //     } else {
  //       let list = [];
  //       list.push(payloadData);
  //       privateChats.set(payloadData.senderName, list);
  //       setPrivateChats(new Map(privateChats));
  //     }
  //   }
  // };

  const onPrivateMessage = (payload) => {
    var payloadData = JSON.parse(payload.body);
    if (privateChats.get(payloadData.senderName)) {
      privateChats.get(payloadData.senderName).push(payloadData);
      setPrivateChats(new Map(privateChats));
    } else {
      let list = [];
      list.push(payloadData);
      privateChats.set(payloadData.senderName, list);
      setPrivateChats(new Map(privateChats));
    }
  };

  const handleMessage = (event) => {
    const { value } = event.target;
    setUserData({ ...userData, message: value });
  };

  const sendValue = () => {
    if (stompClientChat) {
      var chatMessage = {
        senderName: userData.username,
        message: userData.message,
        status: "MESSAGE",
      };
      console.log(chatMessage);
      stompClientChat.send("/app/message", {}, JSON.stringify(chatMessage));
      setUserData({ ...userData, message: "" });
    }
  };

  const sendPrivateValue = () => {
    if (stompClientChat) {
      var chatMessage = {
        senderName: userData.username,
        receiverName: tab,
        message: userData.message,
        status: "MESSAGE",
      };

      if (userData.username !== tab) {
        privateChats.get(tab).push(chatMessage);
        setPrivateChats(new Map(privateChats));
      }
      stompClientChat.send(
        "/app/private-message",
        {},
        JSON.stringify(chatMessage)
      );
      setUserData({ ...userData, message: "" });
    }
  };

  /////////////////////////////////////////// CHAT ///////////////////////////////////////////

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

      {/* CHAT */}
      <button className="chat-button" onClick={connect}>Open chat {">>"}</button>

      {showChat && (
        <div className="container">
          {userData.connected ? (
            <div className="chat-box">
              <div className="member-list">
                <ul>
                  <li
                    onClick={() => {
                      setTab("CHATROOM");
                    }}
                    className={`member ${tab === "CHATROOM" && "active"}`}
                  >
                    Chatroom
                  </li>
                  {[...privateChats.keys()].map((name, index) => (
                    <li
                      onClick={() => {
                        setTab(name);
                      }}
                      className={`member ${tab === name && "active"}`}
                      key={index}
                    >
                      {name}
                    </li>
                  ))}
                </ul>
              </div>
              {tab === "CHATROOM" && (
                <div className="chat-content">
                  <ul className="chat-messages">
                    {publicChats.map((chat, index) => (
                      <li
                        className={`message ${
                          chat.senderName === userData.username && "self"
                        }`}
                        key={index}
                      >
                        {chat.senderName !== userData.username && (
                          <div className="avatar">{chat.senderName}</div>
                        )}
                        <div className="message-data">{chat.message}</div>
                        {chat.senderName === userData.username && (
                          <div className="avatar self">{chat.senderName}</div>
                        )}
                      </li>
                    ))}
                  </ul>

                  <div className="send-message">
                    <input
                      type="text"
                      className="input-message"
                      placeholder="Type something..."
                      value={userData.message}
                      onChange={handleMessage}
                    />
                    <button
                      type="button"
                      className="send-button"
                      onClick={sendValue}
                    >
                      Send
                    </button>
                  </div>
                </div>
              )}
              {tab !== "CHATROOM" && (
                <div className="chat-content">
                  <ul className="chat-messages">
                    {[...privateChats.get(tab)].map((chat, index) => (
                      <li
                        className={`message ${
                          chat.senderName === userData.username && "self"
                        }`}
                        key={index}
                      >
                        {chat.senderName !== userData.username && (
                          <div className="avatar">{chat.senderName}</div>
                        )}
                        <div className="message-data">{chat.message}</div>
                        {chat.senderName === userData.username && (
                          <div className="avatar self">{chat.senderName}</div>
                        )}
                      </li>
                    ))}
                  </ul>

                  <div className="send-message">
                    <input
                      type="text"
                      className="input-message"
                      placeholder="Type something..."
                      value={userData.message}
                      onChange={handleMessage}
                    />
                    <button
                      type="button"
                      className="send-button"
                      onClick={sendPrivateValue}
                    >
                      Send
                    </button>
                  </div>
                </div>
              )}
            </div>
          ) : (
            <div />
          )}
        </div>
      )}

    </div>
   );
}
 
export default UserProfile;