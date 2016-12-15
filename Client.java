import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;
import java.nio.file.Files;






public class Client {
	Socket requestSocket;           //socket connect to the server
	ObjectOutputStream out;         //stream write to the socket
 	ObjectInputStream in;           //stream read from the socket
	String message;                 //message send to the server
	BufferedReader br;
	static int sPort = 8000;
	
	public Client(String name){}

	void run()
	{
		try{
			//create a socket to connect to the server
			requestSocket = new Socket("localhost", sPort);
			System.out.println("Please enter command as a string");
			//initialize inputStream and outputStream
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			
			//get Input from standard input
			br = new BufferedReader(new InputStreamReader(System.in));

			//Start two threads: one for sending and the other for receiving
			final Thread sending = new Thread(() -> {
                            while(true){
                                sendingFunction();
                            }
                        });
			sending.start();
			
			final Thread listening = new Thread(() -> {
                            while(true){
                                listeningFunction();
                            }
                        });
			listening.start();
			TimeUnit.SECONDS.sleep(2000);
		}

		catch (ConnectException e) {
    			System.err.println("Connection refused. You need to initiate a server first.");
		} 
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		
		catch(IOException | InterruptedException e){
		}

		finally{
			//Close connections
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
			}
		}
	}
	
	//send a message to the output stream
	void sendMessage(String msg)
	{
		try{
			//stream write the message
			out.writeObject(msg);
			out.flush();
		}
		catch(IOException ioException){
		}
	}
	
	
	private void sendingFunction(){
		try{
			message= br.readLine();
			out.writeObject(message);
			out.flush();
			System.out.println("Message Sent");
			System.out.println("Please enter command as a string");
		}
		catch(Exception ioException){
		}
	}
	
	private void listeningFunction(){
		try{
			Object obj = in.readObject();
			
			//if it is a message
			if(obj instanceof String[]){
				String[] msg;
                msg = (String[])obj;
				System.out.println("@"+msg[0]+" : "+msg[1]);
			}

			//if it is a file
			else{
				Sender sender= (Sender)obj;
				File file = new File("C:\\cn\\client"+sender.getIndex()+"\\"+sender.getMsg());
				Files.write(file.toPath(), sender.getData());
				System.out.println("File: "+sender.getMsg()+" was sent by "+sender.getName());
			}
		}
				
		catch(Exception ioException){
		} 
	}
	
	//main method
	public static void main(String args[])
	{
		if(args.length > 1)
				sPort = Integer.parseInt(args[1]);

		Client client = new Client(args[0]);
		
		client.run();
	}

}