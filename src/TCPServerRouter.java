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

            //SThread t = new SThread(RoutingTable, clientSocket, ind, currentRouterIP, nextRouterIP); // creates a thread with a random port
            //t.start(); // starts the thread
            //ind++; // increments the index
            System.out.println("ServerRouter connected with Server Router: " + clientSocket.getInetAddress().getHostAddress());


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


        //closing connections
        clientSocket.close();
        serverSocket.close();
    }
}