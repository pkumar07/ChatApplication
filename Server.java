import java.net.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.FileInputStream;


public class Server {

	private static int sPort = 8000;   //The server will be listening on this port number by default
	private static final String fdirectory = "C:\\CN\\";
	private static final ArrayList<ObjectOutputStream> Clients = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {
		if(args.length > 0)
				sPort = Integer.parseInt(args[0]);
		System.out.println("The server is running at port "+sPort); 
        ServerSocket listener = new ServerSocket(sPort);
		int clientNum = 1;
        	try {
            		while(true) {
                		new Handler(listener.accept(),clientNum).start();
				System.out.println("Client client"  + clientNum + " connected");
				clientNum++;
            			}
        	} finally {
            		listener.close();
        	} 
 
    	}

	/**
     	* A handler thread class.  Handlers are spawned from the listening
     	* loop and are responsible for dealing with a single client's requests.
     	*/
    	private static class Handler extends Thread {
        	private String message;    //message received from the client

			private Socket connection;
        	private ObjectInputStream in;	//stream read from the socket
        	private ObjectOutputStream out;    //stream write to the socket
			private int no;		//The index number of the client

        	public Handler(Socket connection, int no) {
            	this.connection = connection;
				this.no = no;
        	}

	        public void run() {
	 		try{
				//initialize Input and Output streams
				out = new ObjectOutputStream(connection.getOutputStream());
				Clients.add(out);
				out.flush();
				
				
				in = new ObjectInputStream(connection.getInputStream());
				try{
					while(true)
					{
						//receive the message sent from the client
						message = (String)in.readObject();
						
						//message split
						String[] words= message.split("\\s",3);
						
						if(words[0].equalsIgnoreCase("broadcast"))
							broadcast(words);
						
						if(words[0].equalsIgnoreCase("unicast"))
							unicast(words);
						
						if(words[0].equalsIgnoreCase("blockcast"))
							blockcast(words);
					}
				}
				catch(ClassNotFoundException classnot){
						System.err.println("Data received in unknown format");
					}
			}
			catch(IOException ioException){
				System.out.println("Looks like client" + no +" has disconnected");
			}
			finally{
				//Close connections
				try{
					in.close();
					out.close();
					connection.close();
				}
				catch(IOException ioException){
					System.out.println("Looks like client" + no +" has disconnected");
				}
			}
		}

		public void broadcast(String[] words){
			
			if(words[1].equalsIgnoreCase("message")){
				String subwords[]=words[2].split("\"");
				System.out.println("client" + no + " broadcasted message");
				String[] msg= new String[10];
				msg[0]= "client"+no;
				msg[1]= subwords[1];
				for(int i=1; i<=Clients.size(); i++){
					if(i!=no){
						sendMessage(Clients.get(i-1),msg);
					}
				}
			}

			else if(words[1].equalsIgnoreCase("file")){
				String subwords[]=words[2].split("\"");	
				System.out.println("client" + no + " broadcasted file");
				try{			
					String[] msg= new String[10];
					msg[0]= "client"+no;
					msg[1]= subwords[1];
					Path path = Paths.get(fdirectory+subwords[1]);
					byte data[] = Files.readAllBytes(path);
					for(int i=1; i<=Clients.size(); i++){
					if(i!=no){
						sendFile(Clients.get(i-1), new Sender(data,msg[1],msg[0],i));
					}
				}
				}catch(Exception e){}		
			}
			
			else System.out.println("Invalid command, broadcast message or file only");
		}
		
		public void unicast(String[] words){
			
			if(words[1].equalsIgnoreCase("message")){
				String subwords[]=words[2].split("\"");
	            subwords[2] = subwords[2].trim();
				System.out.println("client" + no + " unicast message to [" + subwords[2] + "]");
				String[] msg= new String[10];
				msg[0]= "client"+no;
				msg[1]= subwords[1];
				int j= Character.getNumericValue(subwords[2].charAt((subwords[2].length())-1));
				for(int i=1; i<=Clients.size(); i++){
					if(i!=no && i==j){
						sendMessage(Clients.get(i-1),msg);
					}
				}
			}

			else if(words[1].equalsIgnoreCase("file")){
				System.out.println("unicastingcasting file");
				String subwords[]=words[2].split("\"");
				subwords[2] = subwords[2].trim();
				System.out.println("client" + no + " unicast file to [" + subwords[2] + "]");
				int j= Character.getNumericValue(subwords[2].charAt((subwords[2].length())-1));
				try{
					String[] msg= new String[10];
					msg[0]= "client"+no;
					msg[1]= subwords[1];
					Path path = Paths.get(fdirectory+subwords[1]);			
					byte data[] = Files.readAllBytes(path);
					for(int i=1; i<=Clients.size(); i++){
					if(i!=no && i==j){
						sendFile(Clients.get(i-1), new Sender(data,msg[1],msg[0],i));
					}
				}
				}catch(Exception e){}	
			}
			
			else System.out.println("Invalid command, unicast message or file only");
		}
		
		public void blockcast(String[] words){
			
			if(words[1].equalsIgnoreCase("message")){
				String subwords[]=words[2].split("\"");
	            subwords[2] = subwords[2].trim();
				System.out.println("client" + no + " blockcasted message from [" + subwords[2] + "]");	
				String[] msg= new String[10];
				msg[0]= "client"+no;
				msg[1]= subwords[1];
				int j= Character.getNumericValue(subwords[2].charAt((subwords[2].length())-1));
				for(int i=1; i<=Clients.size(); i++){
					if(i!=no && i!=j){
						sendMessage(Clients.get(i-1),msg);
					}
				}
			}

			
			else if(words[1].equalsIgnoreCase("file")){
				
				System.out.println("blockcast file");
				String subwords[]=words[2].split("\"");
				subwords[2] = subwords[2].trim();
				System.out.println("client" + no + " blockcast file to [" + subwords[2] + "]");
				int j= Character.getNumericValue(subwords[2].charAt((subwords[2].length())-1));
				try{
					String[] msg= new String[10];
					msg[0]= "client"+no;
					msg[1]= subwords[1];
					Path path = Paths.get(fdirectory+subwords[1]);			
					byte data[] = Files.readAllBytes(path);
					for(int i=1; i<=Clients.size(); i++){
					if(i!=no && i!=j){
						sendFile(Clients.get(i-1), new Sender(data,msg[1],msg[0],i));
					}
				}
				}catch(Exception e){}	


			}
			else{
				System.out.println("Invalid command, blockcast message or file only");
			}

		}
		
		//send a message to the output stream
		public void sendMessage(ObjectOutputStream out, String[] msg){
			try{
				out.writeObject(msg);
				out.flush();
			}
			catch(IOException ioException){
			}
		}

		//send a file to the output stream
		public void sendFile(ObjectOutputStream out, Sender sender){
			try{
				out.writeObject(sender);
				out.flush();
			}
			catch(Exception ioException){
			}
		}

		public byte[] read(File f){
			InputStream is = null;
	       	ByteArrayOutputStream baos = null;
	    	
	    	try {
	    		int r = 0;
	        	byte[] buffer = new byte[5000];
	        	is = new FileInputStream(f);
	        	baos = new ByteArrayOutputStream();
	        	
	        	while((r= is.read(buffer)) != -1){
	            	baos.write(buffer, 0, r);
	        	}
	    	}

	    	catch (FileNotFoundException ex) {}
            catch (IOException ex) {}
           
	    	finally {
	        	try {
	            	if (baos != null)
	                	baos.close();
	        	
	            	if (is != null)
	                	is.close();
	        	}
	        	
	        	catch (IOException e) {
	        		}
	    	}
	    	return baos.toByteArray();
		}
	}
}