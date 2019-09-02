
/* A Java program for a Client */
import java.net.*; 
import java.io.*; 


// https://github.com/BrendanCReidy/CSCE416.git
public class Client 
{ 
/* initialize socket and input output streams */
private Socket socket = null; 
private BufferedReader input = null; 
private DataOutputStream out = null;
private DataInputStream in = null;

/* constructor to put ip address and port */
public Client(String address, int port) 
{ 
	/* establish a connection */
	try {
		socket = new Socket(address, port); 
	} catch(Exception i) {
		System.out.println("Error in IP or port");
		System.exit(0);
    	}
	System.out.println("Connected"); 

	try { 
		/* takes input from terminal */
		input = new BufferedReader(new InputStreamReader(System.in));
		in = new DataInputStream(
				new BufferedInputStream(socket.getInputStream()));
		/* sends output to the socket */
		out = new DataOutputStream(socket.getOutputStream()); 

	} catch(IOException i) { 
		System.out.println(i); 
	} 
	
	/* string to read message from input */
	String line = ""; 
	
	/* keep reading until "Over" is input */
	while (!line.equals("Over")) { 
		try { 
			line = input.readLine(); 
			out.writeUTF(line);
			System.out.println("Waiting for input from server...");
			line = in.readUTF();
			System.out.println("Got input from server: " + line);
		} catch(Exception i) { 
			System.out.println(i); 
		} 
	} 
	
	/* close the connection */
	try { 
		input.close(); 
		out.close(); 
		socket.close(); 
	} catch(Exception i) {
		System.out.println(i);  
	} 
}

public static void main(String args[]) 
{ 
	if (args.length < 2) {
		System.out.println("Client usage: java Client #IP_address #port_number");
	}
	else {
		Client client = new Client(args[0], Integer.parseInt(args[1])); 
	}
} 

}
