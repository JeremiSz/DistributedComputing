package client;

import javax.swing.*;

public class Presentation {

    public static void main(String[] args) {
        var userInfo = createUserInfo();
        var app = new Application(userInfo);
        int result;
        do {
            var message = JOptionPane.showInputDialog("Message: ");
            var response =  app.echo(message);
            result = JOptionPane.showConfirmDialog(null,
                    response + "; Continue?");
        }while (result == JOptionPane.YES_OPTION);
        app.close();
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
                    DEFAULT_PORT_NUMBER : Integer.parseInt(portNum);;
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
