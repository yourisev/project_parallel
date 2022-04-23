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


        final int FILE_SIZE = 6022386; // file size temporary hard coded
        // should bigger than the file to be downloaded

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
        String fromServer; // messages received from ServerRouter
        String address = "192.168.137.210"; // destination IP (Server)---modify
        long t0, t1, t;


        final int SOCKET_PORT = 13267;  // you may change this
        final String FILE_TO_BE_RECEIVED = "C:\\Users\\Kouede Loic\\OneDrive\\Desktop\\file2.txt";  // you may change this

        // Communication process (initial sends/receives
        out.println(address);// initial send (IP of the destination Server)
        fromServer = in.readLine();//initial receive from router (verification of connection)
        System.out.println("ServerRouter: " + fromServer);

        boolean foundDestination = false;

        System.out.println("Waiting for Server Router's response...");
        while ((fromServer = in.readLine()) != null) {
            System.out.println("Server Router said: " + fromServer);
            if (fromServer.equals(address)) { // exit statement
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
            ServerSocket servsock = null;

            try {
                servsock = new ServerSocket(SOCKET_PORT);
                while (running) {
                    System.out.println("Waiting...");
                    try {
                        sock = servsock.accept();
                        System.out.println("Accepted connection : " + sock);

                        // receive file
                        byte[] myBytearray = new byte[FILE_SIZE];
                        InputStream is = sock.getInputStream();
                        fos = new FileOutputStream(FILE_TO_BE_RECEIVED);
                        bos = new BufferedOutputStream(fos);
                        bytesRead = is.read(myBytearray, 0, myBytearray.length);
                        current = bytesRead;

                        do {
                            bytesRead =
                                    is.read(myBytearray, current, (myBytearray.length - current));
                            if (bytesRead >= 0) current += bytesRead;
                        } while (bytesRead > -1);

                        bos.write(myBytearray, 0, current);
                        bos.flush();
                        System.out.println("File " + FILE_TO_BE_RECEIVED
                                + " downloaded (" + current + " bytes read)");
                        running = false;

                    } finally {
                        if (fos != null) fos.close();
                        if (bos != null) bos.close();
                        if (sock != null) sock.close();
                    }
                }
            } finally {
                if (servsock != null) servsock.close();
            }


            // closing connections
            System.out.println("Closing connections");
            out.close();
            in.close();
            Socket.close();
        }
    }


}
