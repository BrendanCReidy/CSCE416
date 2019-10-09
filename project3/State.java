import java.util.LinkedList;

public class State {
    private LinkedList<String> clients = new LinkedList<String>();
    private LinkedList<Boolean> client_state = new LinkedList<Boolean>();
    private LinkedList<String[]> peerConnections = new LinkedList<String[]>();
    private LinkedList<String[]> connection_requests = new LinkedList<String[]>();
    private LinkedList<String[]> messages = new LinkedList<>();
    public String last_disconnect = "";
    public synchronized void add_message(String name, String aMessage){
        String[] new_message = {name, aMessage};
        messages.add(new_message);
    }
    public synchronized LinkedList<String[]> get_messages(){
        return this.messages;
    }
    public synchronized String[] get_recent_message(){
        return this.messages.getLast();
    }
    public synchronized int get_inbound_requests(String name){
        int index=0;
        for(String[] request : connection_requests){
            String to = request[1];
            if(to.equals(name))
                return index;
            index++;
        }
        return -1;
    }
    public synchronized void add_connection_request(String name, String peer){
        String[] new_message = {name, peer};
        connection_requests.add(new_message);
    }
    public synchronized void remove_connection_request(String name){
        int index = get_inbound_requests(name);
        if(index!=-1)
            connection_requests.remove(index);
    }
    public synchronized LinkedList<String[]> get_connection_requests(){
        return this.connection_requests;
    }
    public synchronized void connect_client(String name){
        clients.add(name);
        client_state.add(false);
    }
    public synchronized void disconnect_client(String clientName){
        int index=0;
        last_disconnect = clientName;
        for(String aClient : clients){
            if(aClient.equals(clientName)){
                clients.remove(index);
                client_state.remove(index);
                disconnect_peers(clientName);
            }
            index++;
        }
    }
    public synchronized LinkedList<String> get_clients(){
        return this.clients;
    }
    public synchronized String get_connection_peer(String peer){
        for(String[] peers : peerConnections){
            String peerA = peers[0];
            String peerB = peers[1];
            if(peerA.equals(peer))
                return peerB;
            else if(peerB.equals(peer))
                return peerA;
        }
        return "";
    }
    public synchronized boolean is_busy(String peer){
        int index=0;
        for(String[] peers : peerConnections){
            String peerA = peers[0];
            String peerB = peers[1];
            if(peerA.equals(peer) || peerB.equals(peer)) {
                return true;
            }
            index++;
        }
        return false;
    }
    public synchronized LinkedList<String[]> get_peer_connections(){
        return this.peerConnections;
    }
    public synchronized boolean client_exists(String peer){
        int index=0;
        for(String aPeer : clients){
            if(aPeer.equals(peer))
                return true;
            index++;
        }
        return false;
    }
    public synchronized void disconnect_peers(String peer){
        int index=0;
        int saveIndex = 0;
        for(String[] peers : peerConnections){
            String peerA = peers[0];
            String peerB = peers[1];
            if(peerA.equals(peer) || peerB.equals(peer))
                saveIndex = index;
            if(peer.equals(peerA))
                client_state.set(index, false);
            if(peer.equals(peerB))
                client_state.set(index, false);
            index++;
        }
        peerConnections.remove(saveIndex);
    }
    public synchronized void connect_peers(String peerA, String peerB){
        String[] peers = {peerA, peerB};
        peerConnections.add(peers);
        int index=0;
        for(String peer : clients){
            if(peer.equals(peerA))
                client_state.set(index, true);
            if(peer.equals(peerB))
                client_state.set(index, true);
            index++;
        }
    }
    public synchronized String toString(){
        String out = "";
        out = "Currently connected: \n";
        int index = 0;
        for(String client_name : clients) {
            String busyString = "free";
            if(client_state.get(index))
                busyString = "busy";
            out += client_name + "\t" + busyString + "\n";
            index++;
        }
        return  out;
    }
}