package client;

public class SMTHelper {
    private final static String LOGIN_TEMPLATE = "command:login,username:%s,password:%s";
    private final static String WRITE_TEMPLATE = "command:write,text:%s";
    private final static String READ_TEMPLATE = "command:read";
    private final static String LOGOUT_TEMPLATE = "command:logout";
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
    public class Responce{
        public int code;
        public String meaning;
    }
}
