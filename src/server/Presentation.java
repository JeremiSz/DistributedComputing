package server;

import java.util.Date;

public class Presentation {
    private StringBuilder builder;

    public Presentation(){
        builder = new StringBuilder();
    }
    public void log(String message){
        String logMessage = (new Date()) + " " + message;
        System.out.println(logMessage);
    }
    public void error(Exception ex){
        System.out.println((new Date()));
        ex.printStackTrace();
    }

}
