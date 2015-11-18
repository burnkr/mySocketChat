package lecture12.mySocketChat;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date dateTime;
    private String text;
    private String from;

    public Message(String from, String text) {
        this.from = from;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getFrom() {
        return from;
    }
}
