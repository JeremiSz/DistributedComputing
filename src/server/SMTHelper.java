package server;

import java.util.Dictionary;
import java.util.Hashtable;

public class SMTHelper {
    public final static String COMMAND = "command";
    public final static String LOGIN_USERNAME = "username";
    public final static String LOGIN_PASSWORD = "password";
    public final static String WRITE_TEXT = "text";
    public final static String COMMAND_LOGIN = "login";
    public final static String COMMAND_READ = "read";
    public final static String COMMAND_WRITE = "write";
    public final static String COMMAND_LOGOUT = "logout";
    private final static String TEMPLATE_ERROR = "command:%s,code:%d,meaning:%s";
    public static Dictionary<String,String> parse(String message){
        var data = new Hashtable<String,String>();
        var tokens = message.split(",");
        for (var token : tokens){
            var pair = token.split(":");
            if (pair.length > 1)
                data.put(pair[0],pair[1]);
        }
        return data;
    }
    public static String sendError(int code){
        String message = null;
        switch (code){
            case(1002) -> message = makeError(COMMAND_LOGIN,code,"Other login error");
            case(1003) -> message = makeError(COMMAND_LOGIN,code,"Login not first command");
            case(1004) -> message = makeError(COMMAND_LOGIN,code,"Unknown username");
            case(1005) -> message = makeError(COMMAND_LOGIN,code,"Invalid password");
            case(2002) -> message = makeError(COMMAND_WRITE,code,"Other write error");
            case(2003) -> message = makeError(COMMAND_WRITE,code,"Missing text attribute");
            case(3002) -> message = makeError(COMMAND_READ,code,"Other read error");
            case(3003) -> message = makeError(COMMAND_READ,code,"Invalid read attribute");
            case(4002) -> message = makeError(COMMAND_LOGOUT,code,"Other logout error");
            case(4003) -> message = makeError(COMMAND_LOGOUT,code,"Had invalid attributes");
        }
        return message;
    }
    private static String makeError(String command,int code, String meaning){
        return TEMPLATE_ERROR.formatted(command,code,meaning);
    }
    public static String successfulLogin(){
        return TEMPLATE_ERROR.formatted(COMMAND_LOGIN,1001,"Login successfully");
    }
    public static String successfulLogout(){return  TEMPLATE_ERROR.formatted(COMMAND_LOGOUT,4001,"Logout successfully");}
    public static String successfulWrite(){return  TEMPLATE_ERROR.formatted(COMMAND_WRITE,2001,"Message received successfully");}
    public static String successfulRead(String[] authors,String[] texts){
        var builder = new StringBuilder(COMMAND).append(":").append(COMMAND_READ).append(",code:3001,").append("author");
        for (var author : authors){
            builder.append(":").append(author);
        }
        builder.append(",texts:");
        for(var text : texts){
            builder.append(":").append(text);
        }
        return builder.toString();
    }

}
