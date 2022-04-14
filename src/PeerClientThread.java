import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class PeerClientThread extends Thread {
    int SockNum; // port number

    // Peer client machine's IP
    String peerServerIP;

    PrintWriter out = null; // for writing to peer client
    BufferedReader in = null; // for reading from peer client


    // Constructor
    PeerClientThread(int sockNum, String serverIP) {
        SockNum = sockNum;
        peerServerIP = serverIP;
    }

    // Run method (will run for each machine that connects to the peer server)
    public void run() {

        try {
            Socket clientSocket = new Socket(peerServerIP, SockNum);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            // Variables for message passing
            Reader reader = new FileReader("C:\\Users\\Kouede Loic\\Desktop\\file.txt");//Modify
            BufferedReader fromFile = new BufferedReader(reader); // reader for the string file
            String fromPeerServer; // messages received from peer server
            String toPeerServer; // messages sent to peer server
            long t0, t1, t;

            // Communication process (initial sends/receives
            out.println(peerServerIP);// initial send (IP of the destination peer server)
            fromPeerServer = in.readLine();//initial receive from router (verification of connection)

            System.out.println("Peer Server: " + fromPeerServer);


            InetAddress addr = InetAddress.getLocalHost();
            String host = addr.getHostAddress();

            out.println(host); // Peer Client sends the IP of its machine as initial send
            t0 = System.currentTimeMillis();

            while ((fromPeerServer = in.readLine()) != null) {
                System.out.println("Peer Server said: " + fromPeerServer);
                t1 = System.currentTimeMillis();
                if (fromPeerServer.equals("Bye.")) // exit statement
                    break;
                t = t1 - t0;
                System.out.println("Cycle time: " + t);

                toPeerServer = fromFile.readLine(); // reading strings from a file
                if (toPeerServer != null) {
                    System.out.println("Client said: " + toPeerServer);
                    out.println(toPeerServer); // sending the strings to the Server via ServerRouter
                    t0 = System.currentTimeMillis();
                }
            }

            // closing connections and streams
            out.close();
            in.close();
            clientSocket.close();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about computer: " + peerServerIP);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + peerServerIP);
            System.exit(1);
        }


    }
}
