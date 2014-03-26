/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed.net;

import distributed.dto.*;
import distributed.main.MainFrame;
import distributed.update.UpdateClient;
import distributed.update.UpdateServer;
import distributed.util.SettingsProvider;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
import org.jgroups.Address;
import org.jgroups.Message;

/**
 *
 * @author steffen
 */
public class MessageProcessor extends Thread {
    
    private static final Logger log = Logger.getLogger(MessageProcessor.class);

    private static MessageProcessor instance;
    private BlockingQueue<Message> jobQueue;
    private boolean isRunning;
    
    private static final int STD_PORT = 56348;

    public static synchronized MessageProcessor getInstance() {
        if (instance == null) {
            instance = new MessageProcessor();
            instance.start();
        }

        return instance;
    }

    private MessageProcessor() {
        jobQueue = new LinkedBlockingQueue<>();
        isRunning = true;
    }

    @Override
    public void run() {
        Message msg;
        while (isRunning) {
            try {
                msg = jobQueue.take();
                // handle incoming messages
                if (msg.getObject() instanceof Address) {
                    System.out.println("Leader is: " + (Address) msg.getObject());
                } else if (msg.getObject() instanceof GroupMessage) {
                    // TODO: store in local database
                    Messenger.getInstance().addGroupMessage((GroupMessage) msg.getObject());
                } else if (msg.getObject() instanceof LeaderMessage) {
                    // TODO: store in local database
                     Messenger.getInstance().addLeaderMessage((LeaderMessage) msg.getObject());
                } else if (msg.getObject() instanceof String) {
                    processStringMessage(msg);
                } else if (msg.getObject() instanceof PrivateMessage) {
                    // TODO: store in local database
                    // extract both user name of the system and receiver name of the private message...
                    String tmpUserName = DistributedCore.getInstance().getUserName();
                    String tmpReceiver = ((PrivateMessage) (msg.getObject())).getReceiver();
                    //... and compare both to see if message was meant for him
                    if (tmpUserName.equals(tmpReceiver)) {
                        Messenger.getInstance().addMessage((PrivateMessage) msg.getObject());                      
                    }                   
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processStringMessage(Message msg) {
        String message = (String) msg.getObject();
        if (message.equals("update")) {
            log.debug("Update request");
            new UpdateServer(STD_PORT).start();
            DistributedCore.getInstance().sendMessage(new Message(msg.getSrc(), "server_opened"));
            log.debug("Updateserver started");
        } else if("server_opened".equals(message)) {
            log.debug("Remote updateserver started");
            new UpdateClient(msg.getSrc().toString(), STD_PORT);
            log.debug("Updateclient started");
        } else if (message.equals("close")) {
            DistributedCore.getInstance().closeConnection();
            System.out.println("Logged out");
        }
    }

    public void end() {
        isRunning = false;
        //TODO Save to job queue
        instance = null;
    }

    public boolean addMessage(Message message) {
        if (!isRunning) {
            return false;
        }

        synchronized (jobQueue) {
            jobQueue.add(message);
        }

        return true;
    }
}
