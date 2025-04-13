# 🌐 Peer-to-Peer Communication via Server Routers

This Java-based project simulates a **distributed network** where clients and servers communicate through **intermediary routers**. If a direct route is unavailable, the system automatically switches to a **peer-to-peer (P2P)** fallback for file transmission.

---

## 🚀 Why This Project is Cool

- 📡 Mimics internet-style **message routing and forwarding**
- 🔁 Automatically falls back to **P2P connections** if the router cannot find a path
- 🧠 Includes dynamic **routing table resolution**
- 📁 Supports **chunked file transmission** between peers
- 🧵 Uses Java multithreading to simulate concurrent client/server communication
- 🔄 Ideal for learning networking, routing, and socket programming in Java

---

## 📦 Components Breakdown

| Component              | Role |
|------------------------|------|
| `TCPClient.java`       | Sends messages and destination IP to the router |
| `TCPServer.java`       | Listens for messages via router and responds |
| `TCPServerRouter.java` | A router that connects to the main router and relays messages |
| `MainTCPServerRouter.java` | The main router that maintains a routing table and handles new connections |
| `SThread.java`         | A thread representing a routing connection; handles message forwarding |
| `TCPPeerClient.java`   | Initiates direct P2P file transfer if the destination is unreachable via routers |
| `TCPPeerServer.java`   | Accepts and processes incoming P2P file transfers |

---

## 🔧 Requirements

- Java JDK 8 or later
- Command-line terminal or IDE (IntelliJ / Eclipse)
- Devices or virtual machines on the **same network** if testing with real IPs
- Optionally, modify IP addresses in code before running:
  - `routerName`
  - `address` (destination IPs)

---

## ▶️ How to Run

1. **Start the Main Router**
   ```bash
   javac MainTCPServerRouter.java SThread.java
   java MainTCPServerRouter
   ```
   
2. **Start the Secondary Router**
   ```bash
   javac TCPServerRouter.java SThread.java
   java TCPServerRouter
   ```
   
3. **Start the Server**
   ```bash
   javac TCPServer.java
   java TCPServer
   ```
   
4. **Start the Client**
   ```bash
   javac TCPClient.java
   java TCPClient
   ```
   
5. **If routing fails**, start Peer-to-Peer:
   - On client side:
     ```bash
     javac TCPPeerClient.java
     java TCPPeerClient
     ```
  - On server side:
    ```bash
    javac TCPPeerServer.java
    java TCPPeerServer
    ```

---

## 📐 Design

```
Client --> Main Router --> Secondary Router --> Server
          ↳ (Fallback) --> Peer-to-Peer Connection
```

- The system attempts normal routing.
- If destination is not in the routing table, it forwards the request to another router.
- If all routers fail, a direct peer connection is established.

![image](https://github.com/user-attachments/assets/6c7db757-8c24-421f-ab13-3c628249cc5e)


---

## 💾 File Transfer
  - File: file.txt or a specified image (CNN.png)
  - Transfer is chunked (e.g., 30 parts)
  - Total transmission time is measured and logged

---

## 🛠 Features & Concepts
 - Java Sockets
 - Routing Tables
 - Threaded Communication (SThread)
 - Peer-to-Peer Fallback Logic
 - Chunked File Transfer
 - Performance Timing

---

## 📌 Notes

- Update routerName and IPs in each file before running if you're using real networked machines.
- Ensure firewall permissions are granted for ports like 5555, 13267, 15000, etc.
- Use localhost IPs (127.0.0.1) for single-machine testing.
