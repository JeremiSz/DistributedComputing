package server;

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
        try (var myConnectionSocket = new ServerSocket(serverPort)) {
            presentation.log("Echo server ready.");
            while (true) {
                presentation.log("Waiting for a connection.");
                var myDataSocket = new MyStreamSocket(myConnectionSocket.accept());
                presentation.log("connection accepted");
                (new Thread(new SMTServerThread(myDataSocket,presentation))).start();
            }
        }
        catch (Exception ex) {
            presentation.error(ex);
        } // end catch
    }
}
