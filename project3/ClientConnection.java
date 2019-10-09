import java.io.*;
import java.net.*;

public class ClientConnection implements Runnable{
    public Socket clientSock = null;
    State state = null;

    public ClientConnection(Socket clientSocket, State state){
        this.clientSock = clientSocket;
        this.state = state;
    }

    public void run(){
        String name = "";
        try{
            DataInputStream input = new DataInputStream(clientSock.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSock.getOutputStream());
            output.writeUTF("Hello. What is your name?\n");
            name = input.readUTF();
            state.connect_client(name);
            Thread stateManager = new Thread(new StateUpdateThread(output, state, name));
            String currentInput = "";
            stateManager.start();
            output.writeUTF(state.toString());
            output.writeUTF("Enter a name to connect to:");
            boolean isBusy = false;
            while(!currentInput.equals("quit")){
                currentInput = input.readUTF();
                if(!isBusy) {
                    if(currentInput.equalsIgnoreCase("y")){
                        int index = state.get_inbound_requests(name);
                        if(index!=-1){
                            String[] request = state.get_connection_requests().get(index);
                            String from = request[0];
                            String to = request[1];
                            state.connect_peers(from, to);
                            state.remove_connection_request(name);
                            output.writeUTF("Connected to " + to);
                            state.add_message(name, " connected to you");
                            isBusy = true;
                        }else{
                            output.writeUTF("You have no inbound requests!");
                        }
                    }
                    else if(!state.client_exists(currentInput)){
                        output.writeUTF(currentInput + " is not connected!");
                    }
                    else if (!state.is_busy(currentInput)) {
                        if(!currentInput.equals(name)) {
                            isBusy = true;
                            state.add_connection_request(name, currentInput);
                            output.writeUTF("asking " + currentInput + " to connect...");
                        }else{
                            output.writeUTF("You cannot connect to yourself!");
                        }
                    } else {
                        output.writeUTF(currentInput + " is busy!");
                    }
                }else{
                    state.add_message(name, currentInput);
                    output.writeUTF(name + ": " + currentInput);
                }
            }
            stateManager.interrupt();
            state.disconnect_client(name);
            int countdown = 5;
            for(int i = countdown;i>0;--i){
                output.writeUTF("Closing in " + i +"...");
                Thread.sleep(1000);
            }
            output.writeUTF("Good bye\n");
            output.close();
            input.close();
        }catch(EOFException e){
            System.out.println("Server closed connection");
            if(name!="")
                state.disconnect_client(name);
        }
        catch (Exception e){
            System.out.println("Client"+e.getMessage());
        }
    }
}