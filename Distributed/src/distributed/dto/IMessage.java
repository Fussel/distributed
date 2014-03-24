/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.dto;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author ygraf
 */
public class IMessage implements Serializable{
   
    public static final String COL_NAME_DATE    = "send_date";
    public static final String COL_NAME_SENDER  = "sender";
    public static final String COL_NAME_MESSAGE = "message";
    public static final String COL_NAME_DIRTY   = "dirty_bit";
    public static final String COL_NAME_ID      = "id";
    
    @DatabaseField(id=true)
    private UUID  messageUUID;
    @DatabaseField(columnName = COL_NAME_DATE)
    private long    sendDate;
    @DatabaseField(columnName = COL_NAME_SENDER)
    private String  sender;
    @DatabaseField(columnName = COL_NAME_MESSAGE, dataType = DataType.BYTE_ARRAY)
    private byte[]  message;
    @DatabaseField(columnName = COL_NAME_DIRTY)
    private boolean dirtyBit;
    
    
    public IMessage() {
        //needed by IMessage
    }
    
    public IMessage(String sender, byte[] message) {
        this.sendDate       = System.currentTimeMillis();
        this.sender         = sender;
        this.message        = message;
        this.messageUUID    = UUID.randomUUID();
    }

    
    public Date getSendDate() {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(sendDate);      
        return cal.getTime();
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate.getTime();
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.messageUUID);
        hash = 59 * hash + (int) (this.sendDate ^ (this.sendDate >>> 32));
        hash = 59 * hash + Objects.hashCode(this.sender);
        hash = 59 * hash + Arrays.hashCode(this.message);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IMessage other = (IMessage) obj;
        if (!Objects.equals(this.messageUUID, other.messageUUID)) {
            return false;
        }
        if (this.sendDate != other.sendDate) {
            return false;
        }
        if (!Objects.equals(this.sender, other.sender)) {
            return false;
        }
        if (!Arrays.equals(this.message, other.message)) {
            return false;
        }
        return true;
    }
    
    
}
