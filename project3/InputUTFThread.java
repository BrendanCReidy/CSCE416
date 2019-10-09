import java.io.DataInputStream;
import java.util.Scanner;

public class InputUTFThread implements Runnable {
    DataInputStream in;
    String out;
    public InputUTFThread(DataInputStream aIn){
        this.in = aIn;
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                out = in.readUTF();
            }catch (Exception e){
                e.printStackTrace();
            }
            Thread.yield();
        }
    }
}
