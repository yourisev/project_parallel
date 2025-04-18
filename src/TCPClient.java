import java.io.*;
import java.net.*;

public class TCPClient {
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

        // Communication process (initial sends/receives
        out.println(address);// initial send (IP of the destination Server)
        fromServer = in.readLine();//initial receive from router (verification of connection)

       if (fromServer.equals("Connected to the router.")) {
            System.out.println("ServerRouter: " + fromServer);
            out.println(host); // Client sends the IP of its machine as initial send
            t0 = System.currentTimeMillis();

            while (running) {
                // Communication while loop
                while ((fromServer = in.readLine()) != null) {
                    System.out.println("Server: " + fromServer);
                    t1 = System.currentTimeMillis();
                    if (fromServer.equals("Bye.")) // exit statement
                        break;

                    else if(fromServer.split(",")[0].equals("accept")){
//                        TCPPeerServer thread = new TCPPeerServer(5558, fromServer.split(",")[1]); // creates a thread with a random port
//                        thread.start();
                        continue;
                    }
                    t = t1 - t0;
                    System.out.println("Cycle time: " + t);

                    fromUser = fromFile.readLine(); // reading strings from a file
                    if (fromUser != null) {
                        System.out.println("Client: " + fromUser);
                        out.println(fromUser); // sending the strings to the Server via ServerRouter
                        t0 = System.currentTimeMillis();
                    }
                }
            }
        }else  if (fromServer.split(",")[0].equals("accept")) {
//           TCPPeerClient thread = new TCPPeerClient(5558, fromServer.split(",")[1]); // creates a thread with a random port
//           thread.start();
       }else{
           System.out.println("No computer exists with IP address: " + address);
       }

        // closing connections
        out.close();
        in.close();
        socket.close();
    }
}
