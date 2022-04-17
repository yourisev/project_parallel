import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPPeerClient extends Thread {
    public static void main(String[] args) throws IOException {

        // Variables for setting up connection and communication
        Socket Socket = null;
        // socket to connect with ServerRouter
        PrintWriter out = null;
        // for writing to ServerRouter
        BufferedReader in = null;
        // for reading form ServerRouter
        InetAddress addr = InetAddress.getLocalHost();
        String host = addr.getHostAddress();

        boolean running = true;

        // Client machine's IP
        String routerName = "DESKTOP-72USF14";
        // ServerRouter host name
        int SockNum = 5555;
        // port number

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
        Reader reader = new FileReader("C:\\Users\\Kouede Loic\\Desktop\\file.txt");
        BufferedReader fromFile = new BufferedReader(reader); // reader for the string file
        String fromServer; // messages received from ServerRouter
        String fromUser; // messages sent to ServerRouter
        String address = "192.168.137.131"; // destination IP (Server)---modify
        long t0, t1, t;


        final int SOCKET_PORT = 13267;  // you may change this
        final String FILE_TO_SEND = "C:\\Users\\Kouede Loic\\Desktop\\file.txt";  // you may change this

        // Communication process (initial sends/receives
        out.println(address);// initial send (IP of the destination Server)
        fromServer = in.readLine();//initial receive from router (verification of connection)
        System.out.println("ServerRouter: " + fromServer);

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
            FileInputStream fis;
            BufferedInputStream bis = null;
            OutputStream os = null;
            ServerSocket servsock = null;
            Socket sock = null;
            try {
                servsock = new ServerSocket(SOCKET_PORT);
                while (true) {
                    System.out.println("Waiting...");
                    try {
                        sock = servsock.accept();
                        System.out.println("Accepted connection : " + sock);
                        // send file
                        File myFile = new File(FILE_TO_SEND);
                        byte[] mybytearray = new byte[(int) myFile.length()];
                        fis = new FileInputStream(myFile);
                        bis = new BufferedInputStream(fis);
                        bis.read(mybytearray, 0, mybytearray.length);
                        os = sock.getOutputStream();
                        System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
                        os.write(mybytearray, 0, mybytearray.length);
                        os.flush();
                        System.out.println("Done.");
                    } finally {
                        if (bis != null) bis.close();
                        if (os != null) os.close();
                        if (sock != null) sock.close();
                    }
                }
            } finally {
                if (servsock != null) servsock.close();
            }
        }

//        if (fromServer.equals("Connected to the router.")) {
//            System.out.println("ServerRouter: " + fromServer);
//            out.println(host); // Client sends the IP of its machine as initial send
//            t0 = System.currentTimeMillis();
//
//            while (running) {
//                // Communication while loop
//                while ((fromServer = in.readLine()) != null) {
//                    System.out.println("Server: " + fromServer);
//                    t1 = System.currentTimeMillis();
//                    if (fromServer.equals("Bye.")) // exit statement
//                        break;
//
//                    else if(fromServer.split(",")[0].equals("accept")){
//                        TCPPeerServer thread = new TCPPeerServer(5558, fromServer.split(",")[1]); // creates a thread with a random port
//                        thread.start();
//                        continue;
//                    }
//                    t = t1 - t0;
//                    System.out.println("Cycle time: " + t);
//
//                    fromUser = fromFile.readLine(); // reading strings from a file
//                    if (fromUser != null) {
//                        System.out.println("Client: " + fromUser);
//                        out.println(fromUser); // sending the strings to the Server via ServerRouter
//                        t0 = System.currentTimeMillis();
//                    }
//                }
//            }
//        }else  if (fromServer.split(",")[0].equals("accept")) {
//            TCPPeerClient thread = new TCPPeerClient(5558, fromServer.split(",")[1]); // creates a thread with a random port
//            thread.start();
//        }else{
//            System.out.println("No computer exist with IP address: " + address);
//        }

        // closing connections
        out.close();
        in.close();
        Socket.close();
    }
//    int SockNum; // port number
//
//    // Peer client machine's IP
//    String peerServerIP;
//
//    PrintWriter out = null; // for writing to peer client
//    BufferedReader in = null; // for reading from peer client
//
//
//    // Constructor
//    TCPPeerClient(int sockNum, String serverIP) {
//        SockNum = sockNum;
//        peerServerIP = serverIP;
//    }
//
//    // Run method (will run for each machine that connects to the peer server)
//    public void run() {
//
//        try {
//            Socket clientSocket = new Socket(peerServerIP, SockNum);
//            out = new PrintWriter(clientSocket.getOutputStream(), true);
//            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//
//            // Variables for message passing
//            Reader reader = new FileReader("C:\\Users\\Kouede Loic\\Desktop\\file.txt");//Modify
//            BufferedReader fromFile = new BufferedReader(reader); // reader for the string file
//            String fromPeerServer; // messages received from peer server
//            String toPeerServer; // messages sent to peer server
//            long t0, t1, t;
//
//            // Communication process (initial sends/receives
//            out.println(peerServerIP);// initial send (IP of the destination peer server)
//            fromPeerServer = in.readLine();//initial receive from router (verification of connection)
//
//            System.out.println("Peer Server: " + fromPeerServer);
//
//
//            InetAddress addr = InetAddress.getLocalHost();
//            String host = addr.getHostAddress();
//
//            out.println(host); // Peer Client sends the IP of its machine as initial send
//            t0 = System.currentTimeMillis();
//
//            while ((fromPeerServer = in.readLine()) != null) {
//                System.out.println("Peer Server said: " + fromPeerServer);
//                t1 = System.currentTimeMillis();
//                if (fromPeerServer.equals("Bye.")) // exit statement
//                    break;
//                t = t1 - t0;
//                System.out.println("Cycle time: " + t);
//
//                toPeerServer = fromFile.readLine(); // reading strings from a file
//                if (toPeerServer != null) {
//                    System.out.println("Client said: " + toPeerServer);
//                    out.println(toPeerServer); // sending the strings to the Server via ServerRouter
//                    t0 = System.currentTimeMillis();
//                }
//            }
//
//            // closing connections and streams
//            out.close();
//            in.close();
//            clientSocket.close();
//
//        } catch (UnknownHostException e) {
//            System.err.println("Don't know about computer: " + peerServerIP);
//            System.exit(1);
//        } catch (IOException e) {
//            System.err.println("Couldn't get I/O for the connection to: " + peerServerIP);
//            System.exit(1);
//        }


}
