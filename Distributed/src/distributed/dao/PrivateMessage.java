/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author steffen
 */
public class PrivateMessage implements Serializable {
    
    private String message;
    private String receiver;
    private String sender;
    private Date sendDate;

    public PrivateMessage(String receiver, String message) {
        this.message = message;
        this.receiver = receiver;
        //TODO Set sender Prop
        sendDate = GregorianCalendar.getInstance().getTime();
    }

    public String getMessage() {
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
