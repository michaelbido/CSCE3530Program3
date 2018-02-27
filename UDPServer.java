/* 
Compilation: javac UDPServer.java
Execution  : java UDPServer <port_number>

Name       : Michael Bido-Chavez (euid: mb0501)
Class      : CSCE 3530

DHCP Program - Server
-----------------------------
Description:
This program acts as a DHCP server that distrubutes
IP addresses to connected clients with a given time.
-----------------------------
*/

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

class UDPServer
{
    private static int port;
	// getters and setters
	public static int getPort() {
		return port;
	}
	public static void setPort(int p) {
		port = p;
    }
    
	public static void main(String args[]) throws Exception
   	{   
           // check if command line input is valid
		if (args.length > 0) {
			try {
				final int p = Integer.parseInt(args[0]);
				setPort(p);
			} 
			catch (NumberFormatException e) {
				System.err.println("Command Line error: " + args[0] + " must be an integer number. Try again.");
				System.exit(1);
			}
		}
		else {
			System.out.println("Please enter a port number: PServer <port_number>");
			System.exit(2);
		}

		// check if it's a valid port range
		if (getPort() < 1024 || getPort() >= 65535) {
			System.out.println("Port number error:" + port + " must be between 1024 and 65535");
			System.exit(3);
        }
        // retreive list of IP addresses in a separate file
        File myFile = new File("IPaddress.txt");
        BufferedReader brFile = new BufferedReader(new FileReader(myFile));
        HashMap<String, Integer> ipAddrMap = new HashMap<String,Integer>();
        String input;
        while((input = brFile.readLine()) != null) {
            ipAddrMap.put(input, -1);
        }
        brFile.close();
        // define the standard lifetime in seconds
        String lifetime = "3600";
        Integer lifetimeInteger = 3600;
        
		int sportno = getPort();/*UDP server port number */
        DatagramSocket serverSocket = new DatagramSocket(sportno);
        System.out.println("...This is a UDP server...");
        System.out.println("...UDP server is listening to the port number " + sportno + "...");
        while(true) {
            // create buffers for datagrams
            byte[] receiveData = new byte[256];
            byte[] receiveData2 = new byte[256];
            byte[] receiveData3 = new byte[256];
            byte[] sendData = new byte[256];
            /* Waiting for client's message */
            System.out.println("\n...Waiting for client's message...\n");
            // accept new UDP packet
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            System.out.println("Received a message from the client...");
            String clientMsg= new String(receivePacket.getData());
            System.out.println(" ------------- DHCP Discover -------------");                        
            System.out.println("Client has sent... ");

            /* Getting the IP address and port number of client */
            InetAddress IPAddress = receivePacket.getAddress(); /*UDP client IP address */
            int cportno = receivePacket.getPort(); /*UDP client port number */

            // DHCP discover, incoming
            // parses the recevied message, gets yiadder and transID
            String parser[] = clientMsg.split(",");
            String yiaddr = parser[0].split(":")[1];
            String transIDString = parser[1].split(":")[1];
            if (yiaddr.equals("0.0.0.0")) {
                System.out.println(parser[0]);
                System.out.println(parser[1]);
            }
            else {
                // System.out.println("No!");
            }
            
            // DHCP offer, outgoing
            // select an available one
            System.out.println("------------- DHCP Offer -------------");            
            Boolean hasBeenFound = false;
            String message = null;
            String message2 = null;
            // find a key (an IP) that is available.
            while (!hasBeenFound) {
                for (Map.Entry<String,Integer> entry : ipAddrMap.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    if (value == -1 && !hasBeenFound) {
                        message = key;
                        hasBeenFound = true;
                        entry.setValue(lifetimeInteger);
                    }
                }
            }
            // create the message
            System.out.println("Responding with offer...");
            message = "yiaddr:" + message + ",Transaction ID:" + transIDString;
            // System.out.println(message);
            
            // /* Sending a message to the client */

            sendData = message.getBytes();
            // System.out.println(sendData);
            // send the message, including the lifetime
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, cportno);
            serverSocket.send(sendPacket);
            message2 = "Lifetime:" + lifetime + ",";
            sendData = message2.getBytes();
            DatagramPacket sendPacket2 = new DatagramPacket(sendData, sendData.length, IPAddress, cportno);
            serverSocket.send(sendPacket2);
            System.out.println("The Offer has been sent to the client...");

            // DHCP Request, incoming
            System.out.println("------------- DHCP Request -------------");
            // accept UDP packet
            DatagramPacket receivePacket2 = new DatagramPacket(receiveData2, receiveData.length);
            serverSocket.receive(receivePacket2);
            //System.out.println("Received a message from the server...");
            String sMsg = new String(receivePacket2.getData());
            System.out.println("Packet pt1, " + sMsg);
        
            DatagramPacket receivePacket3 = new DatagramPacket(receiveData3, receiveData.length);
            serverSocket.receive(receivePacket3);
            //System.out.println("Received a message from the server...");
            String sMsg2 = new String(receivePacket3.getData());
            System.out.println("Packet pt2, " + sMsg2);

            //DHCP Ack, outgoing
            System.out.println("------------- DHCP ACK -------------");

            // send the ACK information
            sendData = sMsg.getBytes();
            DatagramPacket sendPacket3 = new DatagramPacket(sendData, sendData.length, IPAddress, cportno);
            serverSocket.send(sendPacket3);
            message2 = "Lifetime:" + lifetime + ",";
            sendData = sMsg2.getBytes();
            DatagramPacket sendPacket4 = new DatagramPacket(sendData, sendData.length, IPAddress, cportno);
            serverSocket.send(sendPacket4);
            System.out.println("The ACK has been sent to the client...\nComplete.");

        }
    }
}