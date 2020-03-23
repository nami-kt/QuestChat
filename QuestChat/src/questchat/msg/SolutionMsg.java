package questchat.msg;

/**
 *
 * @author katja
 * 
 * Message with player's answer to equation for server
 */
public class SolutionMsg {
    String solution;

    public SolutionMsg() {
    }

    public SolutionMsg(String solution) {
        this.solution = solution;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    @Override
    public String toString() {
        return "SolutionMsg{" + "solution=" + solution + '}';
    }
    
}
