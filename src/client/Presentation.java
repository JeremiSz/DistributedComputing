package client;

import javax.swing.*;
import java.util.Dictionary;

import static java.lang.Math.min;

public class Presentation {

    public static void main(String[] args) {
        var app = makeConnection();
        if (app == null){
            return;
        }
        choiceMenu(app);
    }
    private static int logout(Application app){
        var response = app.close();
        var code = Integer.parseInt(response.get(SMTHelper.ATTR_CODE));
        if (4001 != code){
            JOptionPane.showMessageDialog(null,response.get(SMTHelper.ATTR_MEANING));
            return 2;
        }
        return 3;
    }
    private static void choiceMenu(Application app){
        String[] options = new String[3];
        options[0] = "Write";
        options[1] = "Read";
        options[2] = "Close";
        int choice;
        do{
            choice = JOptionPane.showOptionDialog(null,
                    "Choose option","Choose",
                    JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,
                    null,options,options[0]);
            switch (choice) {
                case (0) -> write(app);
                case (1) -> read(app);
                case (2) -> choice = logout(app);
            }
        }while (choice < 3);
    }
    private static void write(Application app){
        var text = JOptionPane.showInputDialog("Write your message");
        var result = app.write(text);
        var code = Integer.parseInt(result.get(SMTHelper.ATTR_CODE));
        if (code != 2001){
            JOptionPane.showMessageDialog(null,result.get(SMTHelper.ATTR_MEANING));
        }
    }
    private static void read(Application app){
        var result = app.read();
        var code = Integer.parseInt(result.get(SMTHelper.ATTR_CODE));
        if (code != 3001){
            JOptionPane.showMessageDialog(null,result.get(SMTHelper.ATTR_MEANING));
        }
        else{
            var textarea = new StringBuilder();
            var authors = SMTHelper.extractArray(result.get(SMTHelper.ATTR_AUTHORS));
            var texts = SMTHelper.extractArray(result.get(SMTHelper.ATTR_TEXTS));
            var size = min(authors.length,texts.length);
            for (int i = 0;i<size;i++){
                textarea.append(authors[i])
                        .append(":")
                        .append(texts[i])
                        .append("\n");
            }
            JOptionPane.showMessageDialog(null,textarea);
        }
    }

    private static Application makeConnection(){
        Application app;
        UserInfo userInfo = createUserInfo();
        app = Application.AppBuilder(userInfo.hostName, userInfo.portNum);
        if (app == null)
            return app;
        Dictionary<String,String> result;
        boolean loginComplete = false;
        do{
            result = app.login(userInfo.name, userInfo.password);
            var code = Integer.parseInt(result.get(SMTHelper.ATTR_CODE));
            if (code == 1001){
                loginComplete = true;
            }
            else {
                JOptionPane.showMessageDialog(null,result.get(SMTHelper.ATTR_MEANING));
            }

        }while (!loginComplete);
        return app;
    }
    static class UserInfo{
        public String name;
        public String password;
        public int portNum;
        public String hostName;
        final int DEFAULT_PORT_NUMBER = 1234;
        final String DEFAULT_HOST_NAME = "localhost";
        public UserInfo(){}

        public void setPortNum(String portNum) {
            this.portNum =
                    portNum.isEmpty() ?
                    DEFAULT_PORT_NUMBER : Integer.parseInt(portNum);
        }
        public void setHostName(String hostNameIn){
            this.hostName = hostNameIn.isEmpty() ? DEFAULT_HOST_NAME : hostNameIn;
        }
    }
    static UserInfo createUserInfo(){
        UserInfo userInfo = new UserInfo();
        {
            var portIn = JOptionPane.showInputDialog("Port Number:");
            portIn=portIn.trim();
            userInfo.setPortNum(portIn);
        }
        {
            var hostNameIn = JOptionPane.showInputDialog("Hostname:");
            hostNameIn=hostNameIn.trim();
            userInfo.setHostName(hostNameIn);
        }
        {
            String input;
            do{
                input = JOptionPane.showInputDialog("Username:");
                input = input.trim();
            }while (input.isEmpty());
            userInfo.name = input;
        }

        userInfo.password = JOptionPane.showInputDialog("Password:").trim();

        return userInfo;
    }
}
