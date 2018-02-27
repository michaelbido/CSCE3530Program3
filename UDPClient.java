/* 
Compilation: javac UDPClient.java
Execution  : java UDPClient <port_number>

Name       : Michael Bido-Chavez (euid: mb0501)
Class      : CSCE 3530

DHCP Program - Client
-----------------------------
Description:
The client connects to a DHCP server
to begin a DHCP session.
-----------------------------
*/

import java.io.*;
import java.net.*;
import java.util.Random;

class UDPClient
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

        System.out.println("...This is a UDP client...");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int sportno = getPort(); /* UDP server port number */
        DatagramSocket clientSocket = new DatagramSocket();

        // String host = "localhost";
        String host = "cse01.cse.unt.edu";
        InetAddress IPAddress = InetAddress.getByName(host); /* UDP server's IP address */
        // create buffers for data exchange
        byte[] sendData = new byte[256];
        byte[] receiveData = new byte[256];
        byte[] receiveData2 = new byte[256];
        byte[] receiveData3 = new byte[256];
        byte[] receiveData4 = new byte[256];

        /* Generate message for the client */
        // DHCP Discover
        System.out.println("------------- DHCP Discover -------------");   
        // create random ID             
        Random rand = new Random();
        int randomVal = rand.nextInt(2048);
        String myAddr = "0.0.0.0";
        String clientMsg = "yiaddr:" + myAddr + ",Transaction ID:" + Integer.toString(randomVal);

        /* Sending messag to the UDP server */
        // System.out.print("Enter the client's message: ");
        // String clientMsg = br.readLine();
        // sends yiaddr and the trans ID
        sendData = clientMsg.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, sportno);
        clientSocket.send(sendPacket);
        System.out.println("The message has been sent to the server...");


        /* Receiving reply from the UDP server */
        // DHCP Offer
        System.out.println("------------- DHCP Offer -------------");        
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        //System.out.println("Received a message from the server...");
        String serverMsg = new String(receivePacket.getData());
        System.out.println("Server has sent: " + serverMsg);

        DatagramPacket receivePacket2 = new DatagramPacket(receiveData2, receiveData.length);
        clientSocket.receive(receivePacket2);
        //System.out.println("Received a message from the server...");
        String serverMsg2 = new String(receivePacket2.getData());
        System.out.println("Server has sent: " + serverMsg2);
        // delimit the message into parts as necessary
        String parser[] = serverMsg.split(",");
        String yiaddr = parser[0].split(":")[1];
        String transIDString = parser[1].split(":")[1];
        String lifetime = serverMsg2.split(":")[1];
        // System.out.println(parser[0]);
        // System.out.println(parser[1]);
        // System.out.println(serverMsg2);

        // DHCP Request, outgoing
        System.out.println("------------- DHCP Request -------------");
        // incremement the ID as part of the protocol
        int newID = Integer.parseInt(transIDString.trim()) + 1;
        transIDString = Integer.toString(newID);
        // prepare the message
        String message = "yiaddr:" + yiaddr + ",Transaction ID:" + transIDString;
        sendData = message.getBytes();
        //System.out.println(sendData);
        // send the packet to the server
        DatagramPacket sendPacket2 = new DatagramPacket(sendData, sendData.length, IPAddress, sportno);
        clientSocket.send(sendPacket2);
        String message2 = "Lifetime:" + lifetime;
        sendData = message2.getBytes();
        DatagramPacket sendPacket3 = new DatagramPacket(sendData, sendData.length, IPAddress, sportno);
        clientSocket.send(sendPacket3);
        System.out.println("The Request has been sent to the client with new id of "+ newID +"...");


        // DHCP ACK, incoming
        System.out.println("------------- DHCP ACK -------------");
        // accept incoming packet
        DatagramPacket receivePacket3 = new DatagramPacket(receiveData3, receiveData.length);
        clientSocket.receive(receivePacket3);
        //System.out.println("Received a message from the server...");
        String serverMsg3 = new String(receivePacket3.getData());
        System.out.println("Server has sent: " + serverMsg3);

        DatagramPacket receivePacket4 = new DatagramPacket(receiveData4, receiveData.length);
        clientSocket.receive(receivePacket4);
        //System.out.println("Received a message from the server...");
        String serverMsg4= new String(receivePacket4.getData());
        System.out.println("Server has sent: " + serverMsg4);
        // delimit information, for future use.
        parser = serverMsg3.split(",");
        yiaddr = parser[0].split(":")[1];
        transIDString = parser[1].split(":")[1];
        lifetime = serverMsg4.split(":")[1];
        // System.out.println(parser[0]);
        // System.out.println(parser[1]);
        // System.out.println(serverMsg2);
        System.out.println("ACK complete");

        clientSocket.close();
    }
    
    
}