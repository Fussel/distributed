/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.net;

import distributed.dto.GroupMessage;
import distributed.dto.PrivateMessage;

/**
 *
 * @author steffen
 */
public class Messenger {
    
    private static Messenger instance;
    
    public static Messenger getInstance() {
        if(instance != null)
            instance = new Messenger();
        
        return instance;
    }
    
    private void configure() {
        
    }
    
    private Messenger()  {
        this.instance = new Messenger();
    }
    
    
    public void addMessage(PrivateMessage msg) {
        
    }
    
    public void addGroupMessage(GroupMessage msg) {
        
    }
    
    public void sendMessage(PrivateMessage msg) {
        
    }
    
    public void sendGroupMessage(GroupMessage msg) {
        
    }
    
    public static interface MessageCallback {
        void groupMessageReceived(GroupMessage msg);
        void messageReceived(PrivateMessage msg);
    }
    
    public static class UnconfiguredException extends RuntimeException {
        
    }
}
