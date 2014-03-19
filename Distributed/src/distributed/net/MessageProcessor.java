/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed.net;

import distributed.dto.GroupMessage;
import distributed.dto.PrivateMessage;
import distributed.util.SettingsProvider;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.jgroups.Address;
import org.jgroups.Message;

/**
 *
 * @author steffen
 */
public class MessageProcessor extends Thread {

    private static MessageProcessor instance;
    private BlockingQueue<Message> jobQueue;
    private boolean isRunning;

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
                //TODO Do what ever the fuck you want

                if (msg.getObject() instanceof Address) {
                    System.out.println("Leader is: " + (Address) msg.getObject());
                } else if (msg.getObject() instanceof String) {
                    processStringMessage(msg);
                } else if (msg.getObject() instanceof GroupMessage) {
                    System.out.println(SettingsProvider.getInstance().getUserName() + ": " + ((GroupMessage) msg.getObject()).getMessage());
                } else if (msg.getObject() instanceof PrivateMessage) {

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processStringMessage(Message msg) {
        String message = (String) msg.getObject();
        if (message.equals("update")) {
            System.out.println("Update request");
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
