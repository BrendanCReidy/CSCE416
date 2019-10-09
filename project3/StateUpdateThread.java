import java.io.DataOutputStream;

public class StateUpdateThread implements Runnable {
    DataOutputStream out;
    State state;
    String clientName;
    public StateUpdateThread(DataOutputStream aOut, State aState, String aName){
        this.out = aOut;
        this.state = aState;
        this.clientName = aName;
    }

    public void run() {
        int oldClientNum = state.get_clients().size();
        int messageNum = state.get_messages().size();
        int requestNum = state.get_connection_requests().size();
        int peerConnectionNum = state.get_peer_connections().size();
        boolean doRun = true;
        while (!Thread.interrupted()) {
            while(state.get_peer_connections().size()==peerConnectionNum && state.get_clients().size()==oldClientNum && state.get_messages().size()==messageNum && state.get_connection_requests().size()==requestNum);
            try {
                if(doRun) {
                    if (oldClientNum != state.get_clients().size() || state.get_peer_connections().size()!=peerConnectionNum) {
                        int newClientNum = state.get_clients().size();
                        peerConnectionNum = state.get_peer_connections().size();
                        if (oldClientNum > newClientNum) {
                            if (state.last_disconnect.equals(clientName)) {
                                doRun = false;
                            } else {
                                out.writeUTF(state.last_disconnect + " disconnected");
                            }
                        }
                        if (doRun) {
                            out.writeUTF(state.toString());
                            out.writeUTF("Enter a name to connect to:");
                            oldClientNum = newClientNum;
                        }
                    }
                    if (messageNum != state.get_messages().size()) {
                        messageNum = state.get_messages().size();
                        String[] messageContents = state.get_recent_message();
                        String name = messageContents[0];
                        String message = messageContents[1];
                        if (state.get_connection_peer(name).equals(clientName)) {
                            out.writeUTF(name + ": " + message);
                        }
                    }
                    if (state.get_connection_requests().size() > requestNum) {
                        requestNum = state.get_connection_requests().size();
                        String[] newRequest = state.get_connection_requests().getLast();
                        String from = newRequest[0];
                        String to = newRequest[1];
                        if (to.equals(clientName))
                            out.writeUTF(from + " would like to connect with you! accept? (y/n)");
                    } else {
                        requestNum = state.get_connection_requests().size();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            Thread.yield();
        }
    }
}
