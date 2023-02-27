package server;

import sample_code.MyStreamSocket;

import java.io.IOException;
import java.util.Dictionary;

/**
 * Modified sample from EchoServerThread.
 * @author M. L. Liu
 */

public class SMTServerThread implements Runnable{
    MyStreamSocket myDataSocket;
    Presentation presentation;
    String username;
    MessageStorage messageStorage;

    public SMTServerThread(MyStreamSocket myDataSocket,Presentation presentation, MessageStorage messageStorage) {
        this.myDataSocket = myDataSocket;
        this.presentation = presentation;
        this.messageStorage = messageStorage;
    }

    public void run( ) {
        String message;

        boolean done;
        try {
            {
                message = myDataSocket.receiveMessage( );
                presentation.log("message received: "+ message);
                var data = SMTHelper.parse(message);
                done = !isValidLogin(data);
            }
            while (!done) {
                message = myDataSocket.receiveMessage( );
                presentation.log("message received: "+ message);
                var data = SMTHelper.parse(message);

                switch (data.get(SMTHelper.COMMAND)){
                    case (SMTHelper.COMMAND_READ) -> read(data);
                    case (SMTHelper.COMMAND_WRITE) -> write(data);
                    case (SMTHelper.COMMAND_LOGOUT) -> done = logout(data);
                }

            } //end while !done
            presentation.log("closed session");
        }// end try
        catch (Exception ex) {
            presentation.error(ex);
        } // end catch
    } //end run
    private void read(Dictionary<String,String> data) throws IOException{
        if (data.size() > 1){
            myDataSocket.sendMessage(SMTHelper.sendError(3003));
            return;
        }
        String[] authors = messageStorage.getAuthors();
        String[] texts = messageStorage.getTexts();
        myDataSocket.sendMessage(SMTHelper.successfulRead(authors,texts));
    }
    private void write(Dictionary<String,String> data) throws IOException{
        var text = data.get(SMTHelper.WRITE_TEXT);
        if (text == null){
            myDataSocket.sendMessage(SMTHelper.sendError(2003));
            return;
        }
        messageStorage.addMessage(username,text);
        myDataSocket.sendMessage(SMTHelper.successfulWrite());
    }
    private boolean logout(Dictionary<String,String> data) throws IOException {
        if (data.size() > 1){
            myDataSocket.sendMessage(SMTHelper.sendError(4003));
            return false;
        }
        myDataSocket.sendMessage(SMTHelper.successfulLogout());
        return true;
    }
    private boolean isValidLogin(Dictionary<String,String> data) throws IOException {
        var command = data.get(SMTHelper.COMMAND);
        var valid = command != null &&  command.equals(SMTHelper.COMMAND_LOGIN);
        if (!valid){
            myDataSocket.sendMessage(SMTHelper.sendError(1003));
            return false;
        }
        var username = data.get(SMTHelper.LOGIN_USERNAME);
        if(username == null || username.isEmpty()){
            myDataSocket.sendMessage(SMTHelper.sendError(1004));
            return false;
        }
        this.username = username;
        myDataSocket.sendMessage(SMTHelper.successfulLogin());
        return true;
    }

} //end class
