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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author steffen
 */
public abstract class AUpdateServer extends Thread {

    protected static final Logger log = Logger.getLogger(AUpdateServer.class.getName());

    protected List<IMessage> objectBuffer;
    protected List<IMessage> updateQueue;
    protected List<IMessage> sublist;

    protected ServerSocket server;
    protected Socket client;

    protected ObjectInputStream ois;
    protected ObjectOutputStream oos;

    protected boolean inProgress;

    public AUpdateServer(int port) {
        log.debug("Init UpdateServer");
        updateQueue = new ArrayList<>();
        try {
            server = new ServerSocket(port);
            inProgress = true;
        } catch (IOException e) {
            log.error(e);
        }
    }

    protected abstract void prepareGroupMessageList();

    protected abstract void preparePrivateMessageList();

    private void processMessage(Object o) {

        if (o instanceof UpdateProtokoll) {
            UpdateProtokoll msg = (UpdateProtokoll) o;

            switch (msg.getTask()) {
                case LOAD_GROUP_MESSAGES:
                    log.debug("Load GroupMessage");
                    prepareGroupMessageList();

                    if (msg.getObjectCount() == 0) {
                        //TODO Plain update
                        log.debug("Object count is 0");
                        updateQueue.addAll(objectBuffer);
                        sendProtokollMessage(new UpdateProtokoll(
                                UpdateProtokoll.UpdateTask.SUCESSFUL));
                    } else if (msg.getObjectCount() <= objectBuffer.size()) {
                        log.debug("Object count lower equal");
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

                    if (msg.getObjectCount() == 0) {
                        //TODO PLain update
                        sendProtokollMessage(new UpdateProtokoll(
                                UpdateProtokoll.UpdateTask.SUCESSFUL));
                    } else if (msg.getObjectCount() <= objectBuffer.size()) {
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
                    if (checkHash(msg)) {
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

        if (tmpList.hashCode() == up.getHash()) {
            log.debug("hashes equal");
            return true;
        }

        log.debug("hashes unequal");
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
        } catch (IOException io) {
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

            while (inProgress) {
                log.debug("indaloop");
                o = ois.readObject();
                processMessage(o);

            }

            log.debug("Hashes compared");

            sendProtokollMessage(new UpdateProtokoll(UpdateProtokoll.UpdateTask.SEND_GM_REQ));
            o = ois.readObject();

            if (o instanceof UpdateProtokoll) {
                log.debug(((UpdateProtokoll) o).getTask());
            }

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
