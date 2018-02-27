Name       : Michael Bido-Chavez (euid: mb0501)
Due Date   : Oct. 21, 2017
Class      : CSCE 3530

DHCP Program

—————————————————————
Files

Open/decompress the Program3.zip file. Here is what 
is inside the .zip file:

UDPServer.java
UDPClient.java
IPaddress.txt
compile.sh
README.txt

—————————————————————

—————————————————————
Description

The server program acts as a DHCP server that distrubutes
IP addresses to connected clients with a given time.

The client programs acts as a client that connects to a DHCP server
to begin a DHCP session.

Each file has further comments to understand functionality.
—————————————————————

—————————————————————
How to Compile

Place all files within the same directory in the cse01.cse.unt.edu 
machine, and then enter that directory. Then, type the following:

javac UDPClient.java
javac UDPServer.java

Alternatively, you can run attached 'compile.sh' file.
—————————————————————

—————————————————————
How to Execute

Run the server before running the client. Type the following 
lines to execute each program.

java UDPServer <port_number>
java UDPClient <port_number>

For the port number, enter 13004 for the port.
For the server, press Control+C to stop the server and end the process.
—————————————————————