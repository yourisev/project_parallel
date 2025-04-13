
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainTCPServerRouter {
    public static void main(String[] args) throws IOException {
        Socket clientSocket = null; // socket for the thread---add Ip and port
        Object[][] RoutingTable = new Object[10][2]; // routing table Contains Socket and IP address as field
        int SockNum = 5555; // port number
        boolean Running = true;
        int ind = 0; // index in the routing table
        String currentRouterIP = "192.168.137.1", nextRouterIP = "192.168.137.187";


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
                System.out.println("Waiting...");
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
