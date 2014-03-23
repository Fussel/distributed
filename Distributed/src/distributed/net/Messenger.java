/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.net;

import distributed.dto.GroupMessage;
import distributed.dto.IMessage;
import distributed.dto.PrivateMessage;
import java.util.ArrayList;
import java.util.List;
import org.jgroups.Message;

/**
 *
 * @author steffen
 */
public class Messenger {
    
    private static Messenger instance;
    private List<Messenger.MessageCallback> observers;
    
    public static Messenger getInstance() {
        if(instance == null) {
            instance = new Messenger();
        }
        
        return instance;
    }
    
    private Messenger()  {  
        this.observers = new ArrayList<Messenger.MessageCallback>();
    }
    
    /**
     * Add a new private message.
     * 
     * @param msg The private message which was received. 
     */
    public void addMessage(PrivateMessage msg) {
        System.out.println("Messange received");
        
        //TODO Add to database
        
        for(Messenger.MessageCallback o: observers) {
            o.messageReceived(msg);
        }
    }
    
    /**
     * Add a message which was received.
     * 
     * @param msg The received message. 
     */
    public void addGroupMessage(GroupMessage msg) {
        
        //TODO Add to database
        
        for(Messenger.MessageCallback o: observers)  {
            o.messageReceived(msg);
        }
    }
    
    /**
     * Send a private message.
     * 
     * @param msg The message to send 
     */
    public void sendMessage(PrivateMessage msg) {
        //TODO Add to database
        
        //TODO Send dat shit
        Message jMessage = new Message();
        jMessage.setObject(msg);
        DistributedCore.getInstance().sendMessage(jMessage);
    }
    
    /**
     * Send a new group message.
     * 
     * @param msg The message to send. 
     */
    public void sendGroupMessage(GroupMessage msg) {
        //TODO Add to database
        
        //TODO Send that shit
        Message jMessage = new Message();
        jMessage.setObject(msg);
        DistributedCore.getInstance().sendMessage(jMessage);
    }
    
    /**
     * Add a callback to the notifiers list.
     * @param callback The callback to add.
     */
    public void addMessageListener(Messenger.MessageCallback callback) {
        if(!observers.contains(callback))
            observers.add(callback);
    }
    
    public static interface MessageCallback {
        void messageReceived(IMessage msg);
    }
    
    public static class UnconfiguredException extends RuntimeException {
        
    }
}