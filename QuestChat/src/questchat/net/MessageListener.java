package questchat.net;

import questchat.msg.*;

/**
 *
 * @author katja
 * 
 * Interface for handling message on player's side
 */
public interface MessageListener {

    void onLoginRs(LoginRs msg);
    void onTopicInfo(TopicInfo msg);
    void onTaskInfo(TaskInfo msg);
    void onChatMessage(ChatMessageRs msg);
    
    void onTimeout();
}
