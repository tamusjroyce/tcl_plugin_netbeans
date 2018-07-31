package tclconsole;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author dmp
 */

class InputStreamViaThread extends Thread {

   private InputStream stream;
   private String streamName;
   public Boolean isEndOfStreamReached;

    InputStreamViaThread(InputStream stream, String streamName) {
        this.stream = stream;
        this.streamName = streamName;
        isEndOfStreamReached = false;
    }

@Override
    public void run() {
        Scanner streamScan = new Scanner(stream);
        while ( streamScan.hasNextLine()) {
                System.out.println( streamName + ":" + streamScan.nextLine());
        }
        isEndOfStreamReached = true;
    }
}


public class Main {

    
    public static void main(String[] args) {
        try {
            Process p = new ProcessBuilder("tclsh").start();

            InputStreamViaThread tclshOut = new InputStreamViaThread( p.getInputStream(), "out");
            InputStreamViaThread tclshErr = new InputStreamViaThread( p.getErrorStream(), "err");

            tclshOut.start();
            tclshErr.start();

            PrintWriter output = new PrintWriter(p.getOutputStream());
            Scanner in = new Scanner(System.in);

            while ( !tclshErr.isEndOfStreamReached && !tclshOut.isEndOfStreamReached) {
                String command = in.nextLine();
                output.println(command);
                output.flush();
            }

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

}
