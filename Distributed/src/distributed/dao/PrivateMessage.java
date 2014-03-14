/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed.dao;

import distributed.util.SettingsProvider;
import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author steffen
 */
public class PrivateMessage implements Serializable {

    private byte[] message;
    private String receiver;
    private String sender;
    private Date sendDate;

    public PrivateMessage(String receiver, byte[] message) {
        this.message = message;
        this.receiver = receiver;
        sender = SettingsProvider.getInstance().getUserName();
        sendDate = GregorianCalendar.getInstance().getTime();
    }

    public byte[] getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public Date getSendDate() {
        return sendDate;
    }
}
