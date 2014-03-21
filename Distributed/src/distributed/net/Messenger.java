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

/**
 *
 * @author steffen
 */
public class Messenger {
    
    private static Messenger instance;
    private List<Messenger.MessageCallback> observers;
    
    public static Messenger getInstance() {
        if(instance != null)
            instance = new Messenger();
        
        return instance;
    }
    
    private Messenger()  {
        this.instance = new Messenger();
        
        this.observers = new ArrayList<Messenger.MessageCallback>();
    }
    
    
    public void addMessage(PrivateMessage msg) {
        System.out.println("Messange received");
        
        //TODO Add to database
        
        
    }
    
    public void addGroupMessage(GroupMessage msg) {
        
    }
    
    public void sendMessage(PrivateMessage msg) {
        
    }
    
    public void sendGroupMessage(GroupMessage msg) {
        
    }
    
    public void addMessageListener(Messenger.MessageCallback callback) {
        
    }
    
    public static interface MessageCallback {
        void messageReceived(IMessage msg);
    }
    
    public static class UnconfiguredException extends RuntimeException {
        
    }
}
