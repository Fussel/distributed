/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.update;

import distributed.database.DatabaseManager;
import distributed.dto.IMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author steffen
 */
public class IncrementalUpdate implements IDistributedUpdate {
    
    private static final Logger log = Logger.getLogger(IncrementalUpdate.class.getName());
    
    public static class UpdateServer extends Thread {
        
        private ArrayList<IMessage>     objectBuffer;
        private List<IMessage>          updateQueue;
        private ArrayList<IMessage>          sublist;
        
        private ServerSocket            server;
        private Socket                  client;
        
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        
        public UpdateServer(int port) {
            log.debug("Init UpdateServer");
            updateQueue = new ArrayList<>();          
            try {
                server = new ServerSocket(port);
            } catch(IOException e) {
                log.error(e);
            }
        }       
        
        private void prepareGroupMessageList() {
            objectBuffer = new ArrayList<>();
            objectBuffer.addAll(DatabaseManager.getInstance().loadPosts());
            Collections.sort(objectBuffer);
            log.debug("Posts load into object buffer");
        }
        
        private void preparePrivateMessageList() {
            objectBuffer = new ArrayList<>();
            objectBuffer.addAll(DatabaseManager.getInstance().getAllPrivateMessages());
            Collections.sort(objectBuffer);
            log.debug("PrivateMessages added to objectBuffer");
        }
        
        private void processMessage(Object o) {
            
            if(o instanceof UpdateProtokoll) {
                UpdateProtokoll msg = (UpdateProtokoll) o;
                
                switch(msg.getTask()) {
                    case LOAD_GROUP_MESSAGES:
                        prepareGroupMessageList();
                        
                        if(msg.getObjectCount() == 0) {
                            //TODO Plain update
                        } else if(msg.getObjectCount() < objectBuffer.size()) {
                            //TODO build sublist
                        } else {
                            //TODO NOT SUPPORTED IN THIS VERSION
                            //Breaks the entropy paradigm of the incremental update
                            System.out.println("UpdateClient contains more messages as server");
                            
                            sendProtokollMessage(new UpdateProtokoll(
                                UpdateProtokoll.UpdateTask.FAILURE));
                        }
                    case LOAD_PRIVATE_MESSAGES:
                        
                    case FAILURE: 
                        throw new UpdateFailureException();
                    default:
                        log.warn("Unknown message received while update");
                }
            }
        }
        
        /**
         * 
         * @param objectList 
         */
        private void update(ArrayList<IMessage> objectList) {
            
        }
        
        /**
         * Send an UpdateProtokoll to the client;
         * 
         * @param msg The object which should be send.
         */
        private void sendProtokollMessage(UpdateProtokoll msg) {
            try {
                oos.writeObject(msg);
            } catch(IOException io) {
                log.error(io);
            }
        }
        
        @Override
        public void run() {
            try {
                client = server.accept();
                log.debug("Client connected");
                server.close();
                
                ois = new ObjectInputStream(
                    client.getInputStream());
                oos = new ObjectOutputStream(
                    client.getOutputStream());
                
                Object o = ois.readObject();
                
            } catch (IOException e) {
                log.error(e);
            } catch (ClassNotFoundException cnf) {
                log.error(cnf);
            } catch (UpdateFailureException ufe) {
                log.error(ufe);
            } finally {
                //Close streams
            }
        }
    }
    
    public static class UpdateClient extends Thread {
        
        private ArrayList<IMessage> objectBuffer;
        private Socket client;
        
        public UpdateClient(String ip, int port) {
            log.debug("Initialising UpdateClient");
            try {
                client = new Socket(ip, port);
            } catch(IOException e) {
                log.error(e);
            }
        }
        
        private void loadGroupMessages() {
            objectBuffer = new ArrayList<>();
            objectBuffer.addAll(DatabaseManager.getInstance().loadPosts());
            Collections.sort(objectBuffer);
            log.debug("GroupMessages load into objectBuffer");
        }
        
        private void loadPrivateMessages() {
            objectBuffer = new ArrayList<>();
            objectBuffer.addAll(DatabaseManager.getInstance().getAllPrivateMessages());
            Collections.sort(objectBuffer);
            log.debug("PrivateMessages load into objectBuffer");
        }
        
        private void processMessage(Object o) {
            if(o instanceof UpdateProtokoll) {
                UpdateProtokoll msg = (UpdateProtokoll) o;
                
                switch(msg.getTask()) {
                    case FAILURE:
                        throw new UpdateFailureException();
                }
            }
        }
        
        public void run() {
            try {
                ObjectInputStream ois = new ObjectInputStream(
                        client.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(
                        client.getOutputStream());
                
                loadGroupMessages();
                
                oos.writeObject(new UpdateProtokoll(
                        UpdateProtokoll.UpdateTask.LOAD_GROUP_MESSAGES,
                        objectBuffer.size()));
                
            } catch (IOException io) {
                log.error(io);
            } catch (UpdateFailureException ufe) {
                log.error(ufe);
            } finally {
                //TODO Close streams send close
            }
        }
    }
    
    public static class UpdateFailureException extends RuntimeException {
        
        public UpdateFailureException() {
            super();
        }
        
        public UpdateFailureException(String message) {
            super(message);
        }
    }
    
}
