import java.io.DataOutputStream;
import java.util.Scanner;

public class InputThread implements Runnable {
    DataOutputStream out;
    public InputThread(DataOutputStream aOut){
        this.out = aOut;
    }

    public void run() {
        Scanner input = new Scanner(System.in);
        while (!Thread.interrupted()) {
            try {
                String in="";
                if(input!=null) {
                    in = input.nextLine();
                    out.writeUTF(in);
                }
                if(in.equals("quit"))
                    input=null;
            }catch (Exception e){
                e.printStackTrace();
            }
            Thread.yield();
        }
    }
}
