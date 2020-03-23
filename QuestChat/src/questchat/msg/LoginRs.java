package questchat.msg;

import java.util.Arrays;

/**
 *
 * @author katja
 * Response to connecting to server from server to player
 */
public class LoginRs extends Response {
    int id;
    String[] topics;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }

    @Override
    public String toString() {
        return "LoginRs{" + "id=" + id + ", topics=" + Arrays.toString(topics) + '}';
    }
    
}
