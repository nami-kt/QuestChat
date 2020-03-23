package questchat.msg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author katja
 * 
 * Class for converting message into transport format and back
 * JSON format is used
 */
public class Converter {

    private Gson gson;

    public Converter() {
        gson = (new GsonBuilder()).setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .disableHtmlEscaping()
                .create();
    }

    // message from player with login request
    synchronized public LoginRq loginRq(String body){
        return gson.fromJson(body, LoginRq.class);
    }
    
    synchronized public Message toMessage(LoginRq msg){
        String body = gson.toJson(msg);
        return new Message('L',body);
    }
    
    // basic message from server with with request execution status
    synchronized public Response response(String body){
        return gson.fromJson(body, Response.class);
    }

    synchronized public Message toMessage(Response msg){
        String body = gson.toJson(msg);
        return new Message('r',body);
    }

    // message from server to player about login request processing results
    synchronized public LoginRs loginRs(String body){
        return gson.fromJson(body, LoginRs.class);
    }

    synchronized public Message toMessage(LoginRs msg){
        String body = gson.toJson(msg);
        return new Message('l',body);
    }

    // information from server to player with information about the chat-room
    synchronized public TopicInfo topicInfo(String body){
        return gson.fromJson(body, TopicInfo.class);
    }

    synchronized public Message toMessage(TopicInfo msg){
        String body = gson.toJson(msg);
        return new Message('T',body);
    }

    //  request from player to connect to an existing chat-room
    synchronized public EnterRq enterRq(String body){
        return gson.fromJson(body, EnterRq.class);
    }
    
    synchronized public Message toMessage(EnterRq msg){
        String body = gson.toJson(msg);
        return new Message('E',body);
    }

    // request from player to create a new chat-room
    synchronized public CreateTopicRq createTopicRq(String body){
        return gson.fromJson(body, CreateTopicRq.class);
    }
    
    synchronized public Message toMessage(CreateTopicRq msg){
        String body = gson.toJson(msg);
        return new Message('C',body);
    }

    // Information about the task from server to player
    synchronized public TaskInfo taskInfo(String body){
        return gson.fromJson(body, TaskInfo.class);
    }
    
    synchronized public Message toMessage(TaskInfo msg){
        String body = gson.toJson(msg);
        return new Message('Q',body);
    }

    // response of player to the current task for server
    synchronized public SolutionMsg solution(String body){
        return gson.fromJson(body, SolutionMsg.class);
    }
    
    synchronized public Message toMessage(SolutionMsg msg){
        String body = gson.toJson(msg);
        return new Message('S',body);
    }

    // message for chat-room from player to server
    synchronized public ChatMessageRq chatMessageRq(String body){
        return gson.fromJson(body, ChatMessageRq.class);
    }
    
    synchronized public Message toMessage(ChatMessageRq msg){
        String body = gson.toJson(msg);
        return new Message('M',body);
    }

    // message for chat-room from server to player
    synchronized public ChatMessageRs chatMessageRs(String body){
        return gson.fromJson(body, ChatMessageRs.class);
    }
    
    synchronized public Message toMessage(ChatMessageRs msg){
        String body = gson.toJson(msg);
        return new Message('m',body);
    }
}
