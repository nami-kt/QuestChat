package questchat.msg;

/**
 *
 * @author katja
 * 
 * Request to entry existing chat-room
 */
public class EnterRq {
    String topic; // chat-room name

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    
}
