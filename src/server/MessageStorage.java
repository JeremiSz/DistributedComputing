package server;

import java.util.LinkedList;
import java.util.List;

public class MessageStorage {

    public MessageStorage(){
        authors = new LinkedList<>();
        texts = new LinkedList<>();
    }
    private final List<String> authors;
    private final List<String> texts;


    public synchronized void addMessage(String author,String text){
        authors.add(author);
        texts.add(text);
    }
    public String[] getAuthors(){
        var authorsOut = new String[authors.size()];
        return authors.toArray(authorsOut);
    }
    public String[] getTexts(){
        var textsOut = new String[authors.size()];
        return texts.toArray(textsOut);
    }
}
