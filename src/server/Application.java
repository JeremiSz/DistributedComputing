package server;

import sample_code.EchoServerThread;
import sample_code.MyStreamSocket;

import java.net.ServerSocket;

/**
 * Code sampled from EchoServer3 from course content.
 * Modified for use.
 * @author M. L. Liu
 */

public class Application {
    final static int DEFAULT_PORT_NUM = 1234;
    static Presentation presentation;
    public static void main(String[] args) {
        presentation = new Presentation();
        int serverPort;
        serverPort = (args.length == 1 )? Integer.parseInt(args[0]) : Application.DEFAULT_PORT_NUM;
        try {
            ServerSocket myConnectionSocket = new ServerSocket(serverPort);
            presentation.log("Echo server ready.");
            while (true) {
                presentation.log("Waiting for a connection.");
                MyStreamSocket myDataSocket = new MyStreamSocket(myConnectionSocket.accept());
                presentation.log("connection accepted");
                (new Thread(new EchoServerThread(myDataSocket))).start();
            }
        }
        catch (Exception ex) {
            presentation.error(ex);
        } // end catch
    }
}
