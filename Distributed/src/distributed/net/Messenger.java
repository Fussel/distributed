/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed.net;

import distributed.database.DatabaseManager;
import distributed.dto.GroupMessage;
import distributed.dto.IMessage;
import distributed.dto.LeaderMessage;
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
        if (instance == null) {
            instance = new Messenger();
        }

        return instance;
    }

    private Messenger() {
        this.observers = new ArrayList<Messenger.MessageCallback>();
    }

    /**
     * Add a new private message.
     *
     * @param msg The private message which was received.
     */
    public void addMessage(PrivateMessage msg) {

        DatabaseManager.getInstance().insertPrivateMessage(msg);

        for (Messenger.MessageCallback o : observers) {
            o.messageReceived(msg);
        }
    }

    /**
     * Add a message which was received.
     *
     * @param msg The received message.
     */
    public void addGroupMessage(GroupMessage msg) {

        DatabaseManager.getInstance().insertPost(msg);

        for (Messenger.MessageCallback o : observers) {
            o.messageReceived(msg);
        }
    }

     /**
     * Add a leadermessage which was received.
     *
     * @param msg The received message.
     */
    public void addLeaderMessage(LeaderMessage msg) {
        //TODO insert DB
        //DatabaseManager.getInstance().insertPost(msg);

        for (Messenger.MessageCallback o : observers) {
            o.messageReceived(msg);
        }
    }

    /**
     * Send a private message.
     *
     * @param msg The message to send
     */
    public void sendMessage(PrivateMessage msg) {
        DatabaseManager.getInstance().insertPrivateMessage(msg);
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
        DatabaseManager.getInstance().insertPost(msg);
        Message jMessage = new Message();
        jMessage.setObject(msg);
        DistributedCore.getInstance().sendMessage(jMessage);
    }

    /**
     * Send a new leader message.
     *
     * @param msg The message to send.
     */
    public void sendLeaderMessage(LeaderMessage msg) {
        //TODO insert DB
        //DatabaseManager.getInstance().insertPost(msg);
        Message jMessage = new Message();
        jMessage.setObject(msg);
        DistributedCore.getInstance().sendMessage(jMessage);
    }

    /**
     * Add a callback to the notifiers list.
     *
     * @param callback The callback to add.
     */
    public void addMessageListener(Messenger.MessageCallback callback) {
        if (!observers.contains(callback)) {
            observers.add(callback);
            loadOldMessages(callback);
        }
    }
    
    private void loadOldMessages(MessageCallback callback) {
        for(PrivateMessage msg: DatabaseManager.getInstance().getMyPrivateMessages()) {
            callback.messageReceived(msg);
        }
        
        for(GroupMessage msg: DatabaseManager.getInstance().loadUndeletedPosts()) {
            callback.messageReceived(msg);
        }
    }
    
    public void deleteMessage(GroupMessage msg) {
        DatabaseManager.getInstance().markAsDeleted(msg);
        sendGroupMessage(msg);
    }
    
    public void changeMessage(GroupMessage msg, String newMessage) {
        DatabaseManager.getInstance().editMessage(msg, newMessage);
        sendGroupMessage(msg);
    }
    
    public void shareMessage(GroupMessage msg) {
        DatabaseManager.getInstance().setPostAsShared(msg);
    }

    public static interface MessageCallback {

        void messageReceived(IMessage msg);
    }

    public static class UnconfiguredException extends RuntimeException {

    }
}
