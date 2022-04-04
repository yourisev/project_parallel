

import java.net.*;
import java.io.*;

public class TCPServerRouter {
    private PrintWriter outToServerRouter;
    private BufferedReader fromServerRouter;
    private Socket clientSocket;

    public TCPServerRouter(Socket clientSocket, PrintWriter output, BufferedReader input) throws IOException {
        outToServerRouter = output;
        fromServerRouter = input;
        this.clientSocket = clientSocket;
    }

    public PrintWriter getOutToServerRouter() {
        return outToServerRouter;
    }

    public void setOutToServerRouter(PrintWriter outToServerRouter) {
        this.outToServerRouter = outToServerRouter;
    }

    public BufferedReader getFromServerRouter() {
        return fromServerRouter;
    }

    public void setFromServerRouter(BufferedReader fromServerRouter) {
        this.fromServerRouter = fromServerRouter;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public static void main(String[] args) throws IOException {
        Socket clientSocket = null; // socket for the thread---add Ip and port
        Object[][] RoutingTable = new Object[10][2]; // routing table Contains Socket and IP address as field
        int SockNum = 5555; // port number
        boolean Running = true, found = false;
        int ind = 0; // index in the routing table
        PrintWriter toServerRouter = null; // writers (for writing back to the machine and to destination)
        BufferedReader in, fromServerRouter; // reader (for reading from the machine connected to)
        String destination = "", serverRouterMessage = "";
        String currentRouterIP = "192.168.137.138", nextRouterIP = "192.168.137.1";

        TCPServerRouter serverRouterConnection = null;

        // Server Router machine's IP
        String routerName = "DESKTOP-0EN1VER";

        try {
            clientSocket = new Socket(routerName, SockNum);

            toServerRouter = new PrintWriter(clientSocket.getOutputStream());

            fromServerRouter = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            SThread t = new SThread(RoutingTable, clientSocket, ind, currentRouterIP, nextRouterIP); // creates a thread with a random port
            t.start(); // starts the thread
            ind++; // increments the index
            System.out.println("ServerRouter connected with Server Router: " + clientSocket.getInetAddress().getHostAddress());

//            addr = clientSocket.getInetAddress().getHostAddress();
//            RoutingTable[ind][0] = addr; // IP addresses
//            RoutingTable[ind][1] = toClient; // sockets for communication

//            serverRouterConnection = new TCPServerRouter(clientSocket, new PrintWriter(clientSocket.getOutputStream(), true), new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
//
//            System.out.println("Connection to server router " + routerName + " established");
//
//            // Communication process (initial sends/receives)
//            serverRouterConnection.getOutToServerRouter().println(clientSocket.getInetAddress().getHostAddress());// initial send (IP of the destination Server-Router)

        } catch (UnknownHostException e) {
            System.err.println("Don't know about router: " + routerName);
            //System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + routerName);
            //System.exit(1);
        }


        // Accepting connections
        ServerSocket serverSocket = null; // server socket for accepting connections---add Ip and port
        try {
            serverSocket = new ServerSocket(SockNum);
            System.out.println("ServerRouter is Listening on port: " + SockNum + ".");
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + SockNum + ".");
            System.exit(1);
        }

        // Creating threads with accepted connections
        while (Running) {
            try {
                clientSocket = serverSocket.accept();
                SThread t = new SThread(RoutingTable, clientSocket, ind, currentRouterIP, nextRouterIP); // creates a thread with a random port
                t.start(); // starts the thread
                ind++; // increments the index
                System.out.println("ServerRouter connected with Client/Server: " + clientSocket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                System.err.println("Client/Server failed to connect.");
                System.exit(1);
            }
        }//end while

//        // Creating threads with accepted connections
//        while (Running) {
//            try {
//                if (serverRouterConnection != null) {
//                    serverRouterMessage = serverRouterConnection.fromServerRouter.readLine();
//                    if (serverRouterMessage != null) {
//                        String[] messageFromServerRouter = serverRouterMessage.split(",");
//                        if (inRoutingTable(messageFromServerRouter[0], RoutingTable, out)) {
//                            System.out.println("");
//                            out.println("accept " + messageFromServerRouter[1]);
//                        } else {
//                            System.out.println("Destination: " + messageFromServerRouter[0] + " not found");
//                        }
//                    }
//                }
//
//                System.out.println("Waiting for connection request...");
//
//                clientSocket = serverSocket.accept();
//                out = new PrintWriter(clientSocket.getOutputStream(), true);
//                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                RoutingTable[ind][0] = clientSocket.getInetAddress().getHostAddress(); // IP addresses
//                RoutingTable[ind][1] = clientSocket; // sockets for communication
//
//
//                // Initial sends/receives
//                destination = in.readLine(); // initial read (the destination for writing)
//                if (destination.equals(myAddress)) {
//                    serverRouterConnection = new TCPServerRouter(clientSocket, out, in);
//                    System.out.println("Connection to server router " + routerName + " established");
//                } else {
//                    if (inRoutingTable(destination, RoutingTable, null)) {
//                        System.out.println("Found destination: " + destination);
//                        System.out.println("Forwarding to " + destination);
//                        out.println("Connected to the router."); // confirmation of connection
//                        SThread t = new SThread(RoutingTable, clientSocket, ind, out, in); // creates a thread with a random port
//                        t.start(); // starts the thread
//                        ind++; // increments the index
//                        System.out.println("ServerRouter connected with Client/Server: " + clientSocket.getInetAddress().getHostAddress());
//
//                    } else {
//                        System.out.println("Destination: " + destination + " not found");
//                        System.out.println("Forwarding to server router " + routerName);
//                        serverRouterConnection.getOutToServerRouter().println(destination + "," + clientSocket.getInetAddress().getHostAddress());// Forward destination address to Server Router
//                        out.println(destination);
//                    }
//                }
//            } catch (IOException e) {
//                System.err.println("Client/Server failed to connect.");
//                System.exit(1);
//            }
//        }//end while

        //closing connections
        clientSocket.close();
        serverSocket.close();
    }

    public static boolean inRoutingTable(String ipAddress, Object[][] RoutingTable, PrintWriter out) throws IOException {
        boolean found = false;
        // loops through the routing table to find the destination
        for (int i = 0; i < 10; i++) {
            if (ipAddress.equals((String) RoutingTable[i][0])) {
                Socket outSocket = (Socket) RoutingTable[i][1]; // gets the socket for communication from the table
                if (out != null) {
                    out = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer

                }
                found = true;
                break;
            }
        }
        return found;
    }
}