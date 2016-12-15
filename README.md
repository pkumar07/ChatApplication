# ChatApplication

--------------------------------------------FEATURES--------------------------------------------------------------------------------
(1) Broadcast: Any client is able to send a text to the server, which will relay it to all other clients for display.
(2) Broadcast: Any client is able to send a file of any type to the group via the server.
(3) Unicast: Any client is able to send a private message to a specific other client via the server.
(4) Unicast: Any client is able to send a private file of any type to a specific other client via the server.
(5) Blockcast: Any client is able to send a text to all other clients except for one via the sever.


--------------------------------------------RUNNING THE PROGRAM---------------------------------------------------------------------
1) To run Server:
a. To use a specific portnumber, run as below
	java Server portnumber
	eg. java Server 9000
b. To use the default portnumber provided in the program, run as below:
	java Server 

2) To run each Client
Open seperate command prompts for each Client.
Make sure to name the clients as client1, client2, client3 etc. in numerical order.
a. To use a specific portnumber, run as below:
	java Client client1 portnumber
	eg. java Client client1 9000
b. To use the default portnumber provided in the program, run as below:
	eg. java Client client1
  
  
--------------------------------------COMMANDS FOR EXECUTION:------------------------------------------------------------------------
Once all the clients are connected to the Server at a common portnumber,
provide the following commands in order to:

1) Broadcast a message
To broadcast a message to all clients, enter as below in the sender client window:
broadcast message "hello everyone"

2) Broadcast a file
To broadcast a file to all clients, enter ONLY filename and type as shown below in the sender client window (Do not enter the entire file path):
broadcast file "filename.txt"

3) Unicast a message
To unicast a message to a particular client eg. client1, enter as below in the sender client window:
unicast message "hello everyone" client1

4) Unicast a file
To unicast a file to a particular client eg. client1, enter ONLY filename and type as shown below in the sender client window (Do not enter the entire file path):
unicast file "filename.txt" client1

5) Blockcast a message
To blockcast a message from a particular client eg. client1, enter as below in the sender client window:
blockcast message "hello everyone" client1

6) Blockcast a file
To blockcast a file from a particular client eg. client1, enter ONLY filename and type as shown below in the sender client window (Do not enter the entire file path):
blockcast file "filename.txt" client1
