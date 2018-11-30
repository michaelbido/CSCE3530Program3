# DHCP Program
## A DHCP Server to automatically provide and assign IP addresses.


Name       : Michael Bido-Chavez
Class      : CSCE 3530

## Description

The server program acts as a DHCP server that distrubutes
IP addresses to connected clients with a given time.

The client programs acts as a client that connects to a DHCP server
to begin a DHCP session.

Each file has further comments to understand functionality.

## How to Compile

Place all files within the same directory in the cse01.cse.unt.edu 
machine, and then enter that directory. Then, type the following:

javac UDPClient.java
javac UDPServer.java

Alternatively, you can run attached 'compile.sh' file.

## How to Execute

Run the server before running the client. Type the following 
lines to execute each program.

java UDPServer <port_number>
java UDPClient <port_number>

For the port number, enter 13004 for the port.
For the server, press Control+C to stop the server and end the process.
