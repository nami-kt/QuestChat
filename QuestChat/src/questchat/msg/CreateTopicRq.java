package questchat.msg;

/**
 *
 * @author katja
 * 
 * Request to create new chat-room
 */
public class CreateTopicRq {
    String  topic;  // chat-room name
    int     rounds; // number of rounds in the game for current chat-room
    int     time;   // length of one game-round

    public CreateTopicRq() {
    }

    public CreateTopicRq(String topic, int rounds, int time) {
        this.topic = topic;
        this.rounds = rounds;
        this.time = time;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "CreateTopicRq{" + "topic=" + topic + ", rounds=" + rounds + ", time=" + time + '}';
    }
    
    
}
