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
    
    public abstract static class UpdateServer extends Thread implements IDistributedUpdate.Server {
        protected static final Logger log = Logger.getLogger(IncrementalUpdate.UpdateServer.class.getName());
        
        protected List<IMessage>          objectBuffer;
        protected List<IMessage>          updateQueue;
        protected List<IMessage>          sublist;
        
        protected ServerSocket            server;
        protected Socket                  client;
        
        protected ObjectInputStream ois;
        protected ObjectOutputStream oos;
        
        protected boolean inProgress;
        
        public UpdateServer(int port) {
            log.debug("Init UpdateServer");
            updateQueue = new ArrayList<>();          
            try {
                server = new ServerSocket(port);
                inProgress = true;
            } catch(IOException e) {
                log.error(e);
            }
        }       
        
        protected abstract void prepareGroupMessageList(); 
        
        protected abstract void preparePrivateMessageList();
        
        private void processMessage(Object o) {
            
            if(o instanceof UpdateProtokoll) {
                UpdateProtokoll msg = (UpdateProtokoll) o;
                
                switch(msg.getTask()) {
                    case LOAD_GROUP_MESSAGES:
                        log.debug("Load GroupMessage");
                        prepareGroupMessageList();
                        
                        if(msg.getObjectCount() == 0) {
                            //TODO Plain update
                            updateQueue.addAll(objectBuffer);
                            sendProtokollMessage(new UpdateProtokoll(
                                    UpdateProtokoll.UpdateTask.SUCESSFUL));
                        } else if(msg.getObjectCount() <= objectBuffer.size()) {
                            //Adding missing messages and updating changed
                            sublist = objectBuffer.subList(0, msg.getObjectCount());
                            updateQueue.addAll(objectBuffer.subList(msg.getObjectCount(), 
                                    objectBuffer.size()));
                            sendProtokollMessage(new UpdateProtokoll(
                                    UpdateProtokoll.UpdateTask.START_UPDATE));
                        } else {
                            //TODO NOT SUPPORTED IN THIS VERSION
                            //Breaks the entropy paradigm of the incremental update
                            log.error("UpdateClient contains more messages as server");
                            
                            sendProtokollMessage(new UpdateProtokoll(
                                UpdateProtokoll.UpdateTask.FAILURE));
                        }
                    case LOAD_PRIVATE_MESSAGES:
                        log.debug("Load PrivateMessages");
                        preparePrivateMessageList();
                        
                        if(msg.getObjectCount() == 0) {
                            //TODO PLain update
                            sendProtokollMessage(new UpdateProtokoll(
                                    UpdateProtokoll.UpdateTask.SUCESSFUL));
                        } else if(msg.getObjectCount() <= objectBuffer.size()) {
                            sublist = objectBuffer.subList(0, msg.getObjectCount());
                            updateQueue.addAll(objectBuffer.subList(msg.getObjectCount(), 
                                    objectBuffer.size()));
                            sendProtokollMessage(new UpdateProtokoll(
                                    UpdateProtokoll.UpdateTask.START_UPDATE));
                        } else {
                            //TODO Breaks the entropy paradigm
                            sendProtokollMessage(new UpdateProtokoll(
                                UpdateProtokoll.UpdateTask.FAILURE));
                        }
                    case COMPARE_IMSG_HASH:
                        if(checkHash(msg)) {
                            //Hashes equal
                            sendProtokollMessage(new UpdateProtokoll(
                                    UpdateProtokoll.UpdateTask.HASH_EQUALS));
                            log.debug("Hashes of block are equal");
                        } else {
                            //Hashes differ
                            sendProtokollMessage(new UpdateProtokoll(
                                    UpdateProtokoll.UpdateTask.HASH_DIFFERS));
                        }
                    case SUCESSFUL:
                        inProgress = false;
                    case FAILURE: 
                        throw new UpdateFailureException();
                    default:
                        log.warn("Unknown message received while update");
                }
            }
        }
        
        private boolean checkHash(UpdateProtokoll up) {
            List<IMessage> tmpList = sublist.subList(
                    up.getPivot(), up.getPivot() + up.getObjectCount());
            
            if(tmpList.hashCode() == up.getHash())
                return true;
            
            return false;
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
                          
                oos = new ObjectOutputStream(
                    client.getOutputStream());
                ois = new ObjectInputStream(
                    client.getInputStream());
                
                Object o;
                
                while(inProgress) {
                
                    o = ois.readObject();
                    processMessage(o);
                    
                }
                
                log.debug("Hashes compared");
                
                sendProtokollMessage(new UpdateProtokoll(UpdateProtokoll.UpdateTask.SEND_GM_REQ));
                o = ois.readObject();
                
                if(o instanceof UpdateProtokoll)
                    log.debug(((UpdateProtokoll)o).getTask());
                
                oos.writeObject(updateQueue);
                
      
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
    
    public static class UpdateClient extends Thread implements IDistributedUpdate.Client {
        protected static final Logger log = Logger.getLogger(IncrementalUpdate.UpdateClient.class.getName());
        protected ArrayList<IMessage> objectBuffer;
        protected Socket client;
        
        protected ObjectInputStream ois;
        protected ObjectOutputStream oos;
        
        protected String ip;
        protected int port;
        
        public UpdateClient(String ip, int port) {
            log.debug("Initialising UpdateClient");
            this.ip = ip;
            this.port = port;
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
        
        /**
         * Processing the received objects.
         * 
         * @param o
         * @return 
         */
        private UpdateProtokoll processMessage(Object o) {
            if(o instanceof UpdateProtokoll) {
                UpdateProtokoll msg = (UpdateProtokoll) o;
                
                switch(msg.getTask()) {
                    case HASH_EQUALS:
                        return msg;
                    case HASH_DIFFERS:
                        return msg;
                    case START_UPDATE:
                        log.debug("Update triggerd");
                        update(objectBuffer, 0, objectBuffer.size());
                    case SUCESSFUL:
                        log.debug("Update Sucessful");
                        sendUpdateProtokoll(new UpdateProtokoll(UpdateProtokoll.UpdateTask.SUCESSFUL));
                        return msg;
                    case SEND_GM_REQ:
                        try {
                            log.debug("Loading the updateQueue");
                            Object uq = ois.readObject();
                        } catch(ClassNotFoundException cnf) {
                            log.error(cnf);
                        } catch(IOException io) {
                            log.error(io);
                        }
                    case FAILURE:
                        throw new UpdateFailureException();
                }
            }
            return null;
        }
        
        /**
         * 
         * @return 
         */
        private UpdateProtokoll receiveMessage() {
            try {
                Object o = ois.readObject();              
                return processMessage(o);
            } catch(ClassNotFoundException cnf) {
                log.error(cnf);
            } catch(IOException io) {
                log.error(io);
            }
            return null;
        }
        
        private void sendUpdateProtokoll(UpdateProtokoll msg) {
            try {
                oos.writeObject(msg);
            } catch(IOException io) {
                log.error(io);
            }
        }
        
        public void update(List<IMessage> objStack, int pivot, int stackSize) {
            
            //Check || break
            sendUpdateProtokoll(new UpdateProtokoll(
                UpdateProtokoll.UpdateTask.COMPARE_IMSG_HASH, pivot, stackSize, objStack.hashCode()));
            UpdateProtokoll up = receiveMessage();
            if(up == null) {
                log.error("No response redceived");
            } else {
                switch(up.getTask()) {
                    case HASH_EQUALS:
                        log.debug("Block ok");
                        return;
                    case HASH_DIFFERS:
                        log.debug("Block differs from updater. Go on");
                }
            }
            
            int newPivot = objStack.size() / 2;
            //Check upper half
            List<IMessage> tmpUpperList = objStack.subList(newPivot, objStack.size());
            update(tmpUpperList, newPivot, tmpUpperList.size());
            //Check lower half
            List<IMessage> tmpLowerList = objStack.subList(0, newPivot);
            update(tmpLowerList, newPivot - tmpLowerList.size(), 
                    tmpLowerList.size());
        }
        
        public void run() {
            try {
                client = new Socket(ip, port);
                UpdateProtokoll up;
                
                ois = new ObjectInputStream(
                        client.getInputStream());
                oos = new ObjectOutputStream(
                        client.getOutputStream());
                
                loadGroupMessages();
                
                sendUpdateProtokoll(new UpdateProtokoll(
                        UpdateProtokoll.UpdateTask.LOAD_GROUP_MESSAGES,
                        objectBuffer.size()));
                
                up = receiveMessage();
                
                
                
                loadPrivateMessages();
                
                sendUpdateProtokoll(new UpdateProtokoll(
                    UpdateProtokoll.UpdateTask.LOAD_PRIVATE_MESSAGES,
                    objectBuffer.size()));
                
                up = receiveMessage();
                
                sendUpdateProtokoll(new UpdateProtokoll(
                        UpdateProtokoll.UpdateTask.SUCESSFUL));
                
            } catch (IOException io) {
                log.error(io);
            } catch (UpdateFailureException ufe) {
                log.error(ufe);
            } finally {
                log.debug("Update ended");
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