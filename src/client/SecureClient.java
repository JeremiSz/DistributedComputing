package client;

import sample_code.MyStreamSocket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;

public class SecureClient {
    private final MyStreamSocket socket;
    public SecureClient(String hostname, int portNum) throws IOException {
        var sslSocket = (SSLSocket)SSLSocketFactory.getDefault().createSocket(hostname,portNum);
        sslSocket.startHandshake();
        socket = new MyStreamSocket(sslSocket);
    }
    public String sendMessage(String message) throws IOException {
        socket.sendMessage(message);
        return socket.receiveMessage();
    }
    public void done() throws IOException {
        socket.close();
    }
}
