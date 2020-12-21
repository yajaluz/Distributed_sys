import java.io.*;
import java.net.*;
	
public class Produter {
    
static  Socket client = null;
static  ServerSocket server = null;

static  clientThread t[] = new clientThread[10];           

public static void main(String args[]) {

int port_number = Connect.port;

if (args.length < 1)
    {
	System.out.println("Numero da porta="+port_number);
	    } else {
		port_number=Integer.valueOf(args[0]).intValue();
	    }

        try {
	    server = new ServerSocket(port_number);
        }
        catch (IOException e)
	    {System.out.println(e);}
	

	while(true){
	    try {
		client = server.accept();
		for(int i=0; i<=9; i++){
		    if(t[i]==null)
			{
			    (t[i] = new clientThread(client,t)).start();
			    break;
			}
		}
	    }
	    catch (IOException e) {
		System.out.println(e);}
	}
    }
} 


class clientThread extends Thread{
    
    DataInputStream is = null;
    PrintStream os = null;
    Socket client = null;       
    clientThread t[]; 
    
    public clientThread(Socket client, clientThread[] t){
	this.client=client;
        this.t=t;
    }
    
    public void run() 
    {
	    String line;
        String name;
	try{
	    is = new DataInputStream(client.getInputStream());
	    os = new PrintStream(client.getOutputStream());
	    os.println("Seu nickname: ");
    name = is.readLine();
    os.println("Oi "+name+" ! \n Aperte enter /quit para iniciar o bate papo em uma das nossas salas "); 
    for(int i=0; i<=9; i++)
	if (t[i]!=null && t[i]!=this)  
	    t[i].os.println("*** "+name+" acabou de entrar!!! ***" );
    while (true) {
	line = is.readLine();
            if(line.startsWith("/sair")) break; 
	for(int i=0; i<=9; i++)
	    if (t[i]!=null)  t[i].os.println("<"+name+"> "+line); 
    }
    for(int i=0; i<=9; i++)
	if (t[i]!=null && t[i]!=this)  
	    t[i].os.println("*** "+name+" está nos deixando =( !!! ***" );
    
    os.println("*** Volte sempre "+name+" ***"); 

    for(int i=0; i<=9; i++)
	if (t[i]==this) t[i]=null;  
		    is.close();
		    os.close();
		    client.close();
		}
		catch(IOException e){};
	    }
}


