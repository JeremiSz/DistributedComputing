package client;

import javax.swing.*;

public class Presentation {

    public static void main(String[] args) {
        var app = makeConnection();
        choiceMenu(app);
        JOptionPane.showMessageDialog(null,app.close());
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
            }
        }while (choice < 2);
    }
    private static void write(Application app){
        var text = JOptionPane.showInputDialog("Write your message");
        var result = app.write(text);
        if (result == null) result ="Failed to send message";
        JOptionPane.showMessageDialog(null,result);
    }
    private static void read(Application app){
        var result = app.read();
        if (result == null) result = "Failed to retrieve messages";
        JOptionPane.showMessageDialog(null,result);
    }

    private static Application makeConnection(){
        Application app;
        UserInfo userInfo;
        do {
            userInfo = createUserInfo();
            app = Application.AppBuilder(userInfo.hostName, userInfo.portNum);
        } while (app == null);

        var result = app.login(userInfo.name, userInfo.password);
        JOptionPane.showMessageDialog(null,result);
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
