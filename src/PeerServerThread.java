import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class PeerServerThread extends Thread {
    int SockNum; // port number

    // Peer client machine's IP
    String peerClientIP;

    PrintWriter out = null; // for writing to peer client
    BufferedReader in = null; // for reading from peer client

    // Constructor
    PeerServerThread(int sockNum, String clientIP) {
        SockNum = sockNum;
        peerClientIP = clientIP;
    }

    // Run method (will run for each machine that connects to the peer server)
    public void run() {
        boolean running = true;

        while (running) {
            try {
                ServerSocket serverSocket = new ServerSocket(SockNum);
                Socket clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


                // Variables for message passing
                String toPeerClient; // messages sent to peer client
                String fromPeerClient; // messages received from peer client

                // Communication process (initial sends/receives)
                out.println(peerClientIP);// initial send (IP of the destination Client)
                fromPeerClient = in.readLine();// initial receive from router (verification of connection)
                System.out.println("Peer Client: " + fromPeerClient);

                while ((fromPeerClient = in.readLine()) != null) {
                    System.out.println("Peer Client said: " + fromPeerClient);
                    if (fromPeerClient.equals("Bye.")) // exit statement
                        break;
                    toPeerClient = fromPeerClient.toUpperCase(); // converting received message to upper case
                    System.out.println("Peer Server said: " + toPeerClient);
                    out.println(toPeerClient); // sending the converted message back to the Client directly
                }


                // closing connections and streams
                System.out.println("Closing connections and streams");
                out.close();
                in.close();
                clientSocket.close();
                running = false;

            } catch (UnknownHostException e) {
                System.err.println("Don't know about computer: " + peerClientIP);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to: " + peerClientIP);
                System.exit(1);
            }
        }

    }
}
