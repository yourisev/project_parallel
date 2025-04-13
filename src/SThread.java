


import java.io.*;
import java.net.*;


public class SThread extends Thread {
    private Object[][] RTable; // routing table
    private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
    private BufferedReader in; // reader (for reading from the machine connected to)
    private String inputLine, outputLine, destination, addr, currentRouterIP, nextRouterIP; // communication strings
    private Socket outSocket, toTheClient; // socket for communicating with a destination
    private int ind; // index in the routing table

    // Constructor
    SThread(Object[][] Table, Socket toClient, int index, String currentRouteIP, String nextRouteIP) throws IOException {
        toTheClient = toClient;
        out = new PrintWriter(toClient.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
        RTable = Table;
        addr = toClient.getInetAddress().getHostAddress();
        RTable[index][0] = addr; // IP addresses
        RTable[index][1] = toClient; // sockets for communication
        ind = index;
        currentRouterIP = currentRouteIP;
        nextRouterIP = nextRouteIP;
    }

    // Run method (will run for each machine that connects to the ServerRouter)
    public void run() {


        try {
            boolean running = true;
            // check whether it is thread between server routers
            if (ind == 0) {

                String message;
                while (running) {
                    // Communication loop
                    while ((message = in.readLine()) != null) {
                        System.out.println("Server Router said: " + message);
                        String[] messageArray = message.split(",");
                        if (searchDevice(messageArray[0], RTable, ind) == null) {
                            System.out.println("Destination: " + messageArray[0] + " not found");
                            System.out.println("Connection could not be established "); //+ routerName);
                            outSocket = searchDevice(messageArray[1], RTable, ind);
                            outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
                            outTo.println("false," + outSocket.getInetAddress().getHostAddress());// Forward back unsuccessful message to next server router
                            continue;
                        }
                        outTo.println("accept," + messageArray[1]);// Forward back connection message to server device
                        outSocket = searchDevice(messageArray[1], RTable,ind);
                        outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
                        outTo.println("accept," + messageArray[0]);// Forward back unsuccessful message to next server router

                    }// end while
                }

            } else {

                // Initial sends/receives
                destination = in.readLine(); // initial read (the destination for writing)


                System.out.println("Forwarding to " + destination);
                out.println("Connected to the router."); // confirmation of connection

                // waits 10 seconds to let the routing table fill with all machines' information
                try {
                    Thread.currentThread().sleep(10000);
                } catch (InterruptedException ie) {
                    System.out.println("Thread interrupted");
                }


                //Search destination in routing table
                outSocket = searchDevice(destination, RTable,ind);

                if (outSocket == null) {
                    System.out.println("Destination: " + destination + " not found");
                    System.out.println("Forwarding to next server router: "+ nextRouterIP);
                    outSocket = searchDevice(nextRouterIP, RTable,ind);
                    outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
                    outTo.println(destination + "," + toTheClient.getInetAddress().getHostAddress());// Forward destination address to Server Router

                }else{
                    //Writing to Destination
                    //outSocket = searchDevice(destination, RTable,ind);
                    System.out.println("Found destination: " + destination);
                    outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
                    outTo.println(toTheClient.getInetAddress().getHostAddress());
                }
            } // else
        }// end try
        catch (IOException e) {
            System.err.println("Could not listen to socket.");
            System.exit(1);
        }
    }

    // Prefer searchDevice(...) more elaborate
    public static boolean inRoutingTable(String ipAddress, Object[][] RoutingTable, PrintWriter out) throws IOException {
        boolean found = false;
        // loops through the routing table to find the destination
        for (int i = 0; i < 10; i++) {
            if (ipAddress.equals((String) RoutingTable[i][0])) {
                Socket outSocket = (Socket) RoutingTable[i][1]; // gets the socket for communication from the table
                System.out.println("Found destination: " + ipAddress);

                out = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer

                found = true;
                break;
            }
        }
        return found;
    }

    public static Socket searchDevice(String ipAddress, Object[][] RoutingTable, int index) throws IOException {
        Socket outSocket = null;
        // loops through the routing table to find the device
        for (int i = 1; i <= 10; i++) {
            if (ipAddress.equals(RoutingTable[i][0])) {
                outSocket = (Socket) RoutingTable[i][1]; // gets the socket for communication from the table

                break;
            }
        }
        return outSocket;
    }
}
