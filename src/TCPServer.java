

import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        // Variables for setting up connection and communication
        // socket to connect with ServerRouter
        Socket socket = null; //---add Ip and port
        PrintWriter out = null; // for writing to ServerRouter
        BufferedReader in = null; // for reading form ServerRouter
        InetAddress addr = InetAddress.getLocalHost();
        boolean Running = true;
        String host = addr.getHostAddress(); // Server machine's IP
        String routerName = "DESKTOP-72USF14";//"DESKTOP-0EN1VER"; // ServerRouter host name----Modify
        int SockNum = 5555; // port number
        // Tries to connect to the ServerRouter
        try {
            socket = new socket(routerName, SockNum);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about router: " + routerName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + routerName);
            System.exit(1);
        }

        // Variables for message passing
        String fromServer; // messages sent to ServerRouter
        String fromClient; // messages received from ServerRouter
        String address = "192.168.137.103"; // destination IP (Client)--Modify

        // Communication process (initial sends/receives)
        out.println(address);// initial send (IP of the destination Client)
        fromClient = in.readLine();// initial receive from router (verification of connection)
        System.out.println("ServerRouter: " + fromClient);

        while (Running){
        // Communication while loop
        while ((fromClient = in.readLine()) != null) {
            System.out.println("Client said: " + fromClient);
            if (fromClient.equals("Bye.")) // exit statement
                break;
            else if(fromClient.split(",")[0].equals("accept")){
//                TCPPeerServer thread = new TCPPeerServer(5558, fromClient.split(",")[1]); // creates a thread with a random port
//                thread.start();
                continue;
            }
            fromServer = fromClient.toUpperCase(); // converting received message to upper case
            System.out.println("Server said: " + fromServer);
            out.println(fromServer); // sending the converted message back to the Client via ServerRouter
        }}



        // closing connections and streams
        out.close();
        in.close();
        socket.close();
    }
}
