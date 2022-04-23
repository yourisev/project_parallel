import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;

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
        String address = "192.168.137.210"; // destination IP (Client)--Modify

        final int SOCKET_PORT = 13267;      // you may change this
        final String SERVER = "127.0.0.1";  // localhost
        final String
                FILE_TO_RECEIVED = "C:\\Users\\Kouede Loic\\OneDrive\\Desktop\\file3.txt";  // you may change this, I give a
        // different name because i don't want to
        // overwrite the one used by server...

        final int FILE_SIZE = 6022386; // file size temporary hard coded
        // should bigger than the file to be downloaded

        // Communication process (initial sends/receives)
        out.println(address);// initial send (IP of the destination Client)
        fromClient = in.readLine();// initial receive from router (verification of connection)
        System.out.println("ServerRouter: " + fromClient);

        boolean foundDestination = false;

        boolean running = true;


        // Communication while loop
        System.out.println("Waiting for Server Router's response...");
        while ((fromClient = in.readLine()) != null) {
            System.out.println("Client said: " + fromClient);
            if (fromClient.equals(address)) { // exit statement
                foundDestination = true;
                break;
            }
        }



        if (foundDestination) {
            System.out.println("Starting connection with " + address);
            int bytesRead;
            int current = 0;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            Socket sock = null;
            try {
                sock = new Socket(address, SOCKET_PORT);
                System.out.println("Connecting...");
                String extension = FILE_TO_RECEIVED.substring(FILE_TO_RECEIVED.indexOf(".")+1);
                if(extension.equals("txt")) {

                    // receive file
                }else {
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
                }
            } finally {
                if (fos != null) fos.close();
                if (bos != null) bos.close();
                if (sock != null) sock.close();
            }
        }


        // closing connections and streams
        System.out.println("Closing connections...");
        out.close();
        in.close();
        Socket.close();
    }
}
