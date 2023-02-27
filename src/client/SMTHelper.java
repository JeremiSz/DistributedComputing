package client;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class SMTHelper {
    private final static String LOGIN_TEMPLATE = "command:login,username:%s,password:%s";
    private final static String WRITE_TEMPLATE = "command:write,text:%s";
    private final static String READ_TEMPLATE = "command:read";
    private final static String LOGOUT_TEMPLATE = "command:logout";
    public final static String ATTR_CODE = "code";
    public final static String ATTR_MEANING = "meaning";
    public final static String ATTR_AUTHORS = "authors";
    public final static String ATTR_TEXTS = "texts";
    private final static String COMMAND = "command";
    public final static Dictionary<String,String> GENERIC_LOGIN_ERROR = new Hashtable<>(Map.of(ATTR_CODE,"1002",ATTR_MEANING,"Other login error",COMMAND,"login"));
    public final static Dictionary<String,String> GENERIC_WRITE_ERROR = new Hashtable<>(Map.of(ATTR_CODE,"2002",ATTR_MEANING,"Other write error",COMMAND,"write"));
    public final static Dictionary<String,String> GENERIC_READ_ERROR = new Hashtable<>(Map.of(ATTR_CODE,"3002",ATTR_MEANING,"Other read error",COMMAND,"read"));
    public final static Dictionary<String,String> GENERIC_LOGOUT_ERROR = new Hashtable<>(Map.of(ATTR_CODE,"4002",ATTR_MEANING,"Other logout error",COMMAND,"logout"));
    public static String createLogin(String username,String password){
        return String.format(LOGIN_TEMPLATE, username,password);
    }
    public static String createWrite(String text){
        return String.format(WRITE_TEMPLATE,text);
    }
    public static String createRead(){
        return READ_TEMPLATE;
    }
    public static String createLogout(){
        return LOGOUT_TEMPLATE;
    }
    public static Dictionary<String,String> parse(String message){
        var data = new Hashtable<String,String>();
        var tokens = message.split(",");
        for (var token : tokens){
            var delimiter_index = token.indexOf(':');
            var name = token.substring(0,delimiter_index);
            var value = token.substring(delimiter_index + 1);
            data.put(name,value);
        }
        return data;
    }
    public static String[] extractArray(String value){
        return value.split(":");
    }
}
