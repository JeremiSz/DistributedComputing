package server;

import sample_code.MyStreamSocket;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Code sampled from EchoServer3 from course content.
 * Modified for use.
 * @author M. L. Liu
 */

public class Application {
    final static int DEFAULT_PORT_NUM = 1234;
    static Presentation presentation;
    final static String KEY_FILE_NAME = "private_key.jks";
    final static char[] KEY_PASSWORD = "password".toCharArray();
    public static void main(String[] args) {
        presentation = new Presentation();
        var messageStorage = new MessageStorage();
        int serverPort = (args.length == 1)? Integer.parseInt(args[0]) : Application.DEFAULT_PORT_NUM;
        SSLServerSocketFactory socketFactory;
        try {
            socketFactory = socketFactoryFactory();
        }
        catch (UnrecoverableKeyException | CertificateException | KeyStoreException | IOException |
               NoSuchAlgorithmException | KeyManagementException e) {
            presentation.error(e);
            return;
        }

        try (var myConnectionSocket = socketFactory.createServerSocket(serverPort)) {
            presentation.log("Echo server ready.");
            while (true) {
                presentation.log("Waiting for a connection.");
                var myDataSocket = new MyStreamSocket(myConnectionSocket.accept());
                presentation.log("connection accepted");
                (new Thread(new SMTServerThread(myDataSocket,presentation,messageStorage))).start();
            }
        }
        catch (Exception ex) {
            presentation.error(ex);
        } // end catch
    }
    private static SSLServerSocketFactory socketFactoryFactory() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        var keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(KEY_FILE_NAME),KEY_PASSWORD);

        var keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore,KEY_PASSWORD);

        var context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), null,null);

        return context.getServerSocketFactory();
    }
}
