package questchat.data;

import java.util.ArrayList;

/**
 *
 * @author katja
 *
 * Class for storing data of the room
*/
public class TopicData {
    String name;    // chat-room name
    int    rounds;  // number of rounds in the game for current chat-room
    int    timeSec; // length of one game-round in seconds
    ArrayList<Integer> users = new ArrayList<>(); // list of players in the chat-room
    int    curRound = 0; // number of the current round
    Solution solution;
    long startRound;

    // Constructors
    
    public TopicData() {
    }

    public TopicData(String name) {
        this.name = name;
    }

    // Getters and setters for class fields

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getUsers() {
        return users;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getTimeSec() {
        return timeSec;
    }

    public void setTimeSec(int timeSec) {
        this.timeSec = timeSec;
    }

    public int getCurRound() {
        return curRound;
    }

    public void setCurRound(int curRound) {
        this.curRound = curRound;
    }

    public Solution getSolution() {
        return this.solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public long getStartRound() {
        return this.startRound;
    }

    public void setStartRound(long startRound) {
        this.startRound = startRound;
    }

    public String toString() {
        return "TopicData{name=" + this.name + '}';
    }
    
}
