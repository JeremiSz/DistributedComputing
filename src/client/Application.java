package client;

import sample_code.EchoClientHelper2;

import javax.swing.*;
import java.io.IOException;

/**
 * Code sampled from EchoClient2 from course content.
 * Modified for use.
 * @author M. L. Liu
 */
public class Application {
    private final EchoClientHelper2 echoClientHelper;

    public static Application AppBuilder(String hostName, int portNum){
        try{
            return new Application(hostName,portNum);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public String login(String name, String password){
        try {
            var message = SMTHelper.createLogin(name,password);
            return echoClientHelper.getEcho(message);
        }
        catch (Exception ex){
            handleIt(ex);
            return null;
        }
    }
    private Application(String hostName, int portNum) throws IOException {
        echoClientHelper = new EchoClientHelper2(hostName, String.valueOf(portNum));
    }

    private static void handleIt(Exception ex){
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
    public String close(){
        var message = "";
        try {
            message = SMTHelper.createLogout();
            message = echoClientHelper.getEcho(message);
            echoClientHelper.done();
        }
        catch (Exception ex){
            handleIt(ex);
        }
        return message;
    }
    public String write(String text){
        var message = SMTHelper.createWrite(text);
        try {
            return echoClientHelper.getEcho(message);
        }
        catch (Exception ex){
            handleIt(ex);
            return null;
        }
    }
    public String read(){
        var message = SMTHelper.createRead();
        try {
            return echoClientHelper.getEcho(message);
        }
        catch (Exception ex){
            handleIt(ex);
            return null;
        }
    }
}
