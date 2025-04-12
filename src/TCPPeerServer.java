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
        Socket socket = null; //---add Ip and port
        PrintWriter out = null; // for writing to ServerRouter
        BufferedReader in = null; // for reading form ServerRouter
        InetAddress addr = InetAddress.getLocalHost();
        String host = addr.getHostAddress(); // Server machine's IP
        String routerName = "DESKTOP-0EN1VER";//"DESKTOP-0EN1VER"; // ServerRouter host name----Modify
        int SockNum = 5555; // port number
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
        String fromClient; // messages received from ServerRouter
        String address = "192.168.137.187"; // destination IP (Client)--Modify

        final int SOCKET_PORT = 15000;      // you may change this


        // Communication process (initial sends/receives)
        out.println(address);// initial send (IP of the destination Client)
        fromClient = in.readLine();// initial receive from router (verification of connection)
        System.out.println("ServerRouter: " + fromClient);


        boolean running = true;

        final String FILE_TO_SEND = "C:\\Users\\seuga\\OneDrive\\Desktop\\CNN.png";  // you may change this

        boolean foundDestination = false;

        // Communication while loop
        System.out.println("Waiting for Server Router's response...");
        while ((fromClient = in.readLine()) != null) {
            System.out.println("server Router said: " + fromClient);
            if (fromClient.equals(address)) { // exit statement
                foundDestination = true;
                break;
            }
        }


        if (foundDestination) {
            System.out.println("Starting connection with " + address);
            FileInputStream fis;
            BufferedInputStream bis = null;
            OutputStream os = null;
            Socket sock = null;


            try {
                sock = new Socket(address, SOCKET_PORT);
                System.out.println("Connection established: " + sock);


                // send file
                File myFile = new File(FILE_TO_SEND);
                byte[] mybytearray = new byte[(int) myFile.length()];
                fis = new FileInputStream(myFile);
                bis = new BufferedInputStream(fis);
                bis.read(mybytearray, 0, mybytearray.length);
                os = sock.getOutputStream();
                System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
                //###

                int start = 0;
                int end =mybytearray.length;
                int delta = mybytearray.length / 30;
                int remainder = mybytearray.length % 30;
                int count=1;
                long initTime = System.nanoTime();
                while(start != end){
                    if(count!=31) {
                        os.write(mybytearray, start, delta);//mybytearray.length
                        start += delta;
                        System.out.println("Part :" + (count++) + " sent");
                    }else{
                        delta = remainder;
                        os.write(mybytearray, start, delta);
                        start += delta;
                        System.out.println("Part :" + (count++) + " sent");
                    }
                }
                long endTime = System.nanoTime();
                System.out.println("Transmission Time: "+ (endTime-initTime) +" nS");

                //##33
                os.write(mybytearray, 0, mybytearray.length);
                os.flush();
                System.out.println("Done.");


            } finally {
                if (bis != null) bis.close();
                if (os != null) os.close();
                if (sock != null) sock.close();
            }
        }


        // closing connections and streams
        System.out.println("Closing connections...");
        out.close();
        in.close();
        socket.close();
    }
}
