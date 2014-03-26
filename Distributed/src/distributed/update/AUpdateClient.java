/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed.update;

import distributed.dto.IMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author steffen
 */
public abstract class AUpdateClient extends Thread {

    protected static final Logger log = Logger.getLogger(AUpdateClient.class.getName());
    protected ArrayList<IMessage> objectBuffer;
    protected Socket client;

    protected ObjectInputStream ois;
    protected ObjectOutputStream oos;

    protected String ip;
    protected int port;

    public AUpdateClient(String ip, int port) {
        log.debug("Initialising UpdateClient");
        this.ip = ip;
        this.port = port;
    }

    protected abstract void loadGroupMessages();

    protected abstract void loadPrivateMessages();

    /**
     * Processing the received objects.
     *
     * @param o
     * @return
     */
    private UpdateProtokoll processMessage(Object o) {
        if (o instanceof UpdateProtokoll) {
            UpdateProtokoll msg = (UpdateProtokoll) o;
            log.debug("Received: " + msg.getTask());
            switch (msg.getTask()) {
                case HASH_EQUALS:
                    log.debug("Hashvalues equal");
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
                        sendUpdateProtokoll(new UpdateProtokoll(
                                UpdateProtokoll.UpdateTask.ACK));
                        log.debug("Loading the updateQueue");
                        Object uq = ois.readObject();
                        log.debug("UpdateQueue load");
                    } catch (ClassNotFoundException cnf) {
                        log.error(cnf);
                    } catch (IOException io) {
                        log.error(io);
                    }
                break;
                case SEND_PM_REQ:
                    try {
                        sendUpdateProtokoll(new UpdateProtokoll(
                                UpdateProtokoll.UpdateTask.ACK));
                        log.debug("Loading updateQueue");
                        Object uq = ois.readObject();
                        log.debug("UpdateQueue successfully loaded");
                    } catch(ClassNotFoundException cnf) {
                        log.error(cnf);
                    } catch(IOException io) {
                        log.error(io);
                    }
                break;
                case FAILURE:
                    log.debug("Update failed");
                    throw new UpdateFailureException("Update Failure received");
                default:
                    log.warn(msg);
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
        } catch (ClassNotFoundException cnf) {
            log.error(cnf);
        } catch (IOException io) {
            log.error(io);
        }
        return null;
    }

    private void sendUpdateProtokoll(UpdateProtokoll msg) {
        try {
            log.debug("Send: " + msg.getTask());
            oos.writeObject(msg);
        } catch (IOException io) {
            log.error(io);
        }
    }

    public void update(List<IMessage> objStack, int pivot, int stackSize) {

        //Check || break
        sendUpdateProtokoll(new UpdateProtokoll(
                UpdateProtokoll.UpdateTask.COMPARE_IMSG_HASH, pivot, stackSize, objStack.hashCode()));
        
        UpdateProtokoll up = receiveMessage();
        
        if (up == null) {
            log.error("No response redceived");
        } else {
            switch (up.getTask()) {
                case HASH_EQUALS:
                    log.debug("Block ok");
                    return;
                case HASH_DIFFERS:
                    if(stackSize == 1)
                        return;
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

            receiveMessage();
            receiveMessage();

            loadPrivateMessages();

            sendUpdateProtokoll(new UpdateProtokoll(
                    UpdateProtokoll.UpdateTask.LOAD_PRIVATE_MESSAGES,
                    objectBuffer.size()));

            up = receiveMessage();
            up = receiveMessage();

            sendUpdateProtokoll(new UpdateProtokoll(
                    UpdateProtokoll.UpdateTask.SUCESSFUL));
            
            up = receiveMessage();

        } catch (IOException io) {
            log.error(io);
        } catch (UpdateFailureException ufe) {
            log.error(ufe);
        } finally {
            log.debug("Update ended");
        }
    }
}
