import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPPeerServer extends Thread {
    public static void main(String[] args) throws IOException {
        // Variables for setting up connection and communication
        // socket to connect with ServerRouter
        Socket Socket = null; //---add Ip and port
        PrintWriter out = null; // for writing to ServerRouter
        BufferedReader in = null; // for reading form ServerRouter
        InetAddress addr = InetAddress.getLocalHost();
        String host = addr.getHostAddress(); // Server machine's IP
        String routerName = "DESKTOP-72USF14";//"DESKTOP-0EN1VER"; // ServerRouter host name----Modify
        int SockNum = 5555; // port number
        // Tries to connect to the ServerRouter
        try {
            Socket = new Socket(routerName, SockNum);
            out = new PrintWriter(Socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
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

        final int SOCKET_PORT = 13267;      // you may change this
        final String SERVER = "127.0.0.1";  // localhost
        final String
                FILE_TO_RECEIVED = "C:\\Users\\Kouede Loic\\Desktop\\file3.txt";  // you may change this, I give a
        // different name because i don't want to
        // overwrite the one used by server...

        final int FILE_SIZE = 6022386; // file size temporary hard coded
        // should bigger than the file to be downloaded

        // Communication process (initial sends/receives)
        out.println(address);// initial send (IP of the destination Client)
        fromClient = in.readLine();// initial receive from router (verification of connection)
        System.out.println("ServerRouter: " + fromClient);

        // Set a timeout for waiting an answer from the server router
        long start = System.currentTimeMillis();
        long end = start + 30 * 1000;
        boolean foundDestination = false;
        while (System.currentTimeMillis() < end) {
            // Some expensive operation on the item.
            if (in.readLine().equals(address)) {
                foundDestination = true;
            }
        }

        if (foundDestination) {
            int bytesRead;
            int current = 0;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            Socket sock = null;
            try {
                sock = new Socket(SERVER, SOCKET_PORT);
                System.out.println("Connecting...");

                // receive file
                byte[] mybytearray = new byte[FILE_SIZE];
                InputStream is = sock.getInputStream();
                fos = new FileOutputStream(FILE_TO_RECEIVED);
                bos = new BufferedOutputStream(fos);
                bytesRead = is.read(mybytearray, 0, mybytearray.length);
                current = bytesRead;

                do {
                    bytesRead =
                            is.read(mybytearray, current, (mybytearray.length - current));
                    if (bytesRead >= 0) current += bytesRead;
                } while (bytesRead > -1);

                bos.write(mybytearray, 0, current);
                bos.flush();
                System.out.println("File " + FILE_TO_RECEIVED
                        + " downloaded (" + current + " bytes read)");
            } finally {
                if (fos != null) fos.close();
                if (bos != null) bos.close();
                if (sock != null) sock.close();
            }
        }

//        while (Running){
//            // Communication while loop
//            while ((fromClient = in.readLine()) != null) {
//                System.out.println("Client said: " + fromClient);
//                if (fromClient.equals("Bye.")) // exit statement
//                    break;
//                else if(fromClient.split(",")[0].equals("accept")){
//                    TCPPeerServer thread = new TCPPeerServer(5558, fromClient.split(",")[1]); // creates a thread with a random port
//                    thread.start();
//                    continue;
//                }
//                fromServer = fromClient.toUpperCase(); // converting received message to upper case
//                System.out.println("Server said: " + fromServer);
//                out.println(fromServer); // sending the converted message back to the Client via ServerRouter
//            }}


        // closing connections and streams
        out.close();
        in.close();
        Socket.close();
    }
//    int SockNum; // port number
//
//    // Peer client machine's IP
//    String peerClientIP;
//
//    PrintWriter out = null; // for writing to peer client
//    BufferedReader in = null; // for reading from peer client
//
//    // Constructor
//    TCPPeerServer(int sockNum, String clientIP) {
//        SockNum = sockNum;
//        peerClientIP = clientIP;
//    }
//
//    // Run method (will run for each machine that connects to the peer server)
//    public void run() {
//        boolean running = true;
//
//        while (running) {
//            try {
//                ServerSocket serverSocket = new ServerSocket(SockNum);
//                Socket clientSocket = serverSocket.accept();
//                out = new PrintWriter(clientSocket.getOutputStream(), true);
//                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//
//                // Variables for message passing
//                String toPeerClient; // messages sent to peer client
//                String fromPeerClient; // messages received from peer client
//
//                // Communication process (initial sends/receives)
//                out.println(peerClientIP);// initial send (IP of the destination Client)
//                fromPeerClient = in.readLine();// initial receive from router (verification of connection)
//                System.out.println("Peer Client: " + fromPeerClient);
//
//                while ((fromPeerClient = in.readLine()) != null) {
//                    System.out.println("Peer Client said: " + fromPeerClient);
//                    if (fromPeerClient.equals("Bye.")) // exit statement
//                        break;
//                    toPeerClient = fromPeerClient.toUpperCase(); // converting received message to upper case
//                    System.out.println("Peer Server said: " + toPeerClient);
//                    out.println(toPeerClient); // sending the converted message back to the Client directly
//                }
//
//
//                // closing connections and streams
//                System.out.println("Closing connections and streams");
//                out.close();
//                in.close();
//                clientSocket.close();
//                running = false;
//
//            } catch (UnknownHostException e) {
//                System.err.println("Don't know about computer: " + peerClientIP);
//                System.exit(1);
//            } catch (IOException e) {
//                System.err.println("Couldn't get I/O for the connection to: " + peerClientIP);
//                System.exit(1);
//            }
//        }
//
//    }
}
