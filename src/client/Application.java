package client;

import sample_code.EchoClientHelper2;

import javax.swing.*;
import java.io.InputStreamReader;

/**
 * Code sampled from EchoClient2 from course content.
 * Modified for use.
 * @author M. L. Liu
 */
public class Application {
    private EchoClientHelper2 echoClientHelper;
    public String echo(String message){
        String result = "";
        try {
            result = echoClientHelper.getEcho(message);
        }
        catch (Exception ex){
          handleIt(ex);
        }
        return result.isEmpty()?"oops":result;
    }
    public Application(Presentation.UserInfo info){
        try{
            echoClientHelper = new EchoClientHelper2(info.hostName, String.valueOf(info.portNum));
        }
        catch (Exception ex){
            handleIt(ex);
        }
    }
    private void handleIt(Exception ex){
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
    public void close(){
        try {
            echoClientHelper.done();
        }
        catch (Exception ex){
            handleIt(ex);
        }
    }
}
