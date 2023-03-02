package server;

import sample_code.MyStreamSocket;

import java.io.IOException;
import java.util.Dictionary;

/**
 * Modified sample from EchoServerThread.
 * @author M. L. Liu
 */

public class SMTServerThread implements Runnable{
    final MyStreamSocket myDataSocket;
    final Presentation presentation;
    String username;
    final MessageStorage messageStorage;
    private final SMTHelper helper;

    public SMTServerThread(MyStreamSocket myDataSocket,Presentation presentation, MessageStorage messageStorage) {
        this.myDataSocket = myDataSocket;
        this.presentation = presentation;
        this.messageStorage = messageStorage;
        helper = new SMTHelper();
    }

    public void run( ) {
        String message;

        boolean done;
        try {
            {
                message = myDataSocket.receiveMessage( );
                presentation.log("message received: "+ message);
                var data = helper.parse(message);
                done = !isValidLogin(data);
            }
            while (!done) {
                message = myDataSocket.receiveMessage( );
                presentation.log("message received: "+ message);
                var data = helper.parse(message);

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
            myDataSocket.sendMessage(helper.sendError(3003));
            return;
        }
        String[] authors = messageStorage.getAuthors();
        String[] texts = messageStorage.getTexts();
        myDataSocket.sendMessage(helper.successfulRead(authors,texts));
    }
    private void write(Dictionary<String,String> data) throws IOException{
        var text = data.get(SMTHelper.WRITE_TEXT);
        if (text == null){
            myDataSocket.sendMessage(helper.sendError(2003));
            return;
        }
        messageStorage.addMessage(username,text);
        myDataSocket.sendMessage(helper.successfulWrite());
    }
    private boolean logout(Dictionary<String,String> data) throws IOException {
        if (data.size() > 1){
            myDataSocket.sendMessage(helper.sendError(4003));
            return false;
        }
        myDataSocket.sendMessage(helper.successfulLogout());
        return true;
    }
    private boolean isValidLogin(Dictionary<String,String> data) throws IOException {
        var command = data.get(SMTHelper.COMMAND);
        var valid = command != null &&  command.equals(SMTHelper.COMMAND_LOGIN);
        if (!valid){
            myDataSocket.sendMessage(helper.sendError(1003));
            return false;
        }
        var username = data.get(SMTHelper.LOGIN_USERNAME);
        if(username == null || username.isEmpty()){
            myDataSocket.sendMessage(helper.sendError(1004));
            return false;
        }
        this.username = username;
        myDataSocket.sendMessage(helper.successfulLogin());
        return true;
    }

} //end class
