package client;

import javax.swing.*;
import java.io.IOException;
import java.util.Dictionary;

/**
 * Code sampled from EchoClient2 from course content.
 * Modified for use.
 * @author M. L. Liu
 */
public class Application {
    private final SecureClient client;
    private final SMTHelper helper;

    public static Application AppBuilder(String hostName, int portNum){
        try{
            return new Application(hostName,portNum);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public Dictionary<String,String> login(String name, String password){
        try {
            var message = helper.createLogin(name,password);
            var response = client.sendMessage(message);
            return helper.parse(response);

        }
        catch (Exception ex){
            handleIt(ex);
            return SMTHelper.GENERIC_LOGIN_ERROR;
        }
    }
    private Application(String hostName, int portNum) throws IOException {
        client = new SecureClient(hostName, portNum);
        helper = new SMTHelper();
    }

    private static void handleIt(Exception ex){
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
    public Dictionary<String,String> close(){
        try {
            var message = helper.createLogout();
            var response = client.sendMessage(message);
            client.done();
            return helper.parse(response);
        }
        catch (Exception ex){
            handleIt(ex);
            return SMTHelper.GENERIC_LOGOUT_ERROR;
        }
    }
    public Dictionary<String,String> write(String text){
        text = text.replace(":","");
        text = text.replace(",","");
        text = text.replace("\n","");
        var message = helper.createWrite(text);
        try {
            var response = client.sendMessage(message);
            return helper.parse(response);
        }
        catch (Exception ex){
            handleIt(ex);
            return SMTHelper.GENERIC_WRITE_ERROR;
        }
    }
    public Dictionary<String,String> read(){
        var message = helper.createRead();
        try {
            var response = client.sendMessage(message);
            return helper.parse(response);
        }
        catch (Exception ex){
            handleIt(ex);
            return SMTHelper.GENERIC_READ_ERROR;
        }
    }
}
