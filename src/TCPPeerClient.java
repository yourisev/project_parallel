import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPPeerClient extends Thread {
    public static void main(String[] args) throws IOException {

        // Variables for setting up connection and communication
        Socket socket = null;
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
            socket = new Socket(routerName, SockNum);
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

        boolean foundDestination = false;

        while ((fromServer = in.readLine()) != null) {
            System.out.println("Server: " + fromServer);
            if (fromServer.equals(address)) { // exit statement
                foundDestination = true;
                break;
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
                while (running) {
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
                        running = false;
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

        // closing connections
        System.out.println("Closing connections");
        out.close();
        in.close();
        socket.close();
    }


}
