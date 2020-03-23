package questchat.msg;

/**
 *
 * @author katja
 * 
 * Chat message from player for server
 */
public class ChatMessageRq {
    String message;

    public ChatMessageRq() {
    }

    public ChatMessageRq(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatMessage{" + "message=" + message + '}';
    }
    
    
}
