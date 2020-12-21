import java.io.*;
import java.net.*;

public class Consumer implements Runnable{
    
    static Socket clientSocket = null;
    static PrintStream os = null;
    static DataInputStream is = null;
    static BufferedReader inputLine = null;
    static boolean closed = false;
    
public static void main(String[] args) {
	int port=Connect.port;
        String host="localhost";
	
	if (args.length < 2)
	    {
		System.out.println("Usando="+host+", Numero da porta="+port);
	    } else {
		host=args[0];
		port=Integer.valueOf(args[1]).intValue();
	    }

	try {
            clientSocket = new Socket(host, port);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new PrintStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Host desconhecido "+host);
        } catch (IOException e) {
            System.err.println("Não poderá se conectar a este host "+host);
        }

	
        if (clientSocket != null && os != null && is != null) {
            try {
                new Thread(new Consumer()).start();
		
		while (!closed) {
                    os.println(inputLine.readLine()); 
                }
		
		os.close();
		is.close();
		clientSocket.close();   
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }           
    
public void run() {		
	
	String responseLine;

	try{ 
	    while ((responseLine = is.readLine()) != null) {
		System.out.println(responseLine);
		if (responseLine.indexOf("*** Bye") != -1) break;
	    }
            closed=true;
	} catch (IOException e) {
	    System.err.println("IOException:  " + e);
	}
    }
}
