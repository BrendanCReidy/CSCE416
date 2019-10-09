import java.io.*;
import java.net.*;

public class Server implements Runnable{
    int port = 4321;
    ServerSocket serverSock = null;
    public volatile boolean active = true;

    State state = new State();
    public Server(int aPort){
        this.port = aPort;
    }
    public void run(){
        try{
            this.serverSock = new ServerSocket(this.port);
            serverSock.setSoTimeout(1000);
        }
        catch(IOException e){
            throw new RuntimeException("cannot open port " + port);
        }

        while(this.active){
            Socket clientSock =  null;
            try{
                clientSock = this.serverSock.accept();
                new Thread( new ClientConnection(clientSock,state) ).start();
            } catch(SocketTimeoutException e){
                //do nothing
            }catch(IOException e){
                System.out.println(e.getMessage());
            }


        }

    }
}