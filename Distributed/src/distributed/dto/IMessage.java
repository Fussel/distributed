/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.dto;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author ygraf
 */
public class IMessage implements Serializable{
   
    public static final String COL_NAME_DATE    = "send_date";
    public static final String COL_NAME_SENDER  = "sender";
    public static final String COL_NAME_MESSAGE = "message";
    
    @DatabaseField()
    private String  messageUUID;
    @DatabaseField(columnName = COL_NAME_DATE)
    private Date    sendDate;
    @DatabaseField(columnName = COL_NAME_SENDER)
    private String  sender;
    @DatabaseField(columnName = COL_NAME_MESSAGE, dataType = DataType.BYTE_ARRAY)
    private byte[]  message;
    
    
    public IMessage() {
        //needed by IMessage
    }
    
    public IMessage(String sender, byte[] message) {
        this.sendDate   = GregorianCalendar.getInstance().getTime();
        this.sender     = sender;
        this.message    = message;
    }

    
    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }
    
}
