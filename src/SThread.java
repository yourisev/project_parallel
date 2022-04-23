


import java.io.*;
import java.net.*;
import java.lang.Exception;


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
                        Socket ingoingSocket = searchDevice(messageArray[0], RTable, ind);
                        if (ingoingSocket == null) {
                            System.out.println("Destination: " + messageArray[0] + " not found");
                             System.out.println("Connection could not be established "); //+ routerName);
//                            outSocket = searchDevice(messageArray[1], RTable, ind);
//                            outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
//                            outTo.println("false," + outSocket.getInetAddress().getHostAddress());// Forward back unsuccessful message to next server router
                            continue;
                        }
                        outSocket = ingoingSocket;
                        //outTo.println("accept," + messageArray[1]);// Forward back connection message to server device
                        //outSocket = searchDevice(messageArray[1], RTable,ind);
                        outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
                        outTo.println(messageArray[1]);// Forward back unsuccessful message to next server router

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


//                // loops through the routing table to find the destination
//                for (int i = 0; i < 10; i++) {
//                    if (destination.equals((String) RTable[i][0])) {
//                        outSocket = (Socket) RTable[i][1]; // gets the socket for communication from the table
//                        System.out.println("Found destination: " + destination);
//                        outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
//                    }
//                }

                //The below part was the communication loop now the communication will be direct
//                while (running) {
//                    // Communication loop
//                    while ((inputLine = in.readLine()) != null) {
//                        System.out.println("Client/Server said: " + inputLine);
//                        if (inputLine.equals("Bye.")) // exit statement
//                            break;
//                        else if (inputLine.split(",")[0].equals("false")) {
//                            outSocket = searchDevice(inputLine.split(",")[1], RTable,ind);
//                            outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
//                            inputLine = "false";
//                        }
//                        outputLine = inputLine; // passes the input from the machine to the output string for the destination
//
//                        if (outSocket != null) {
//                            outTo.println(outputLine); // writes to the destination
//                        }
//                    }// end while
//                }
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
        for (int i = 0; i < 10; i++) {
            System.out.println("Sthread - Index " + index);
           System.out.println("Ip address:" + RoutingTable[i][0] + "at index " + i);
            if (ipAddress.equals(RoutingTable[i][0])) {
                outSocket = (Socket) RoutingTable[i][1]; // gets the socket for communication from the table

                break;
            }
        }
        return outSocket;
    }
}