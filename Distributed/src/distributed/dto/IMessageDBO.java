/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;

/**
 *
 * @author ygraf
 */
@DatabaseTable(tableName = "messages")
public class IMessageDBO implements Serializable {

    public static final String TABLE_NAME           = "massages";
    
    public static final String COL_NAME_ID          = "id";
    public static final String COL_NAME_DIRTY_BIT   = "dirty_bit";
    public static final String COL_NAME_MESSAGE     = "massage";
    
    @DatabaseField(columnName = COL_NAME_ID, generatedId = true)
    private int      messageID;
    @DatabaseField(columnName = COL_NAME_DIRTY_BIT)
    private boolean  dirtyBit;
    @DatabaseField(columnName = COL_NAME_MESSAGE)
    private IMessage message;
    
    
    public IMessageDBO() {
        //needed by ORMLite
    }
    
    public IMessageDBO(int messageID, boolean dirtyBit, IMessage message) {
        this.messageID  = messageID;
        this.dirtyBit   = dirtyBit;
        this.message    = message;
    }

    
    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public boolean isDirtyBit() {
        return dirtyBit;
    }

    public void setDirtyBit(boolean dirtyBit) {
        this.dirtyBit = dirtyBit;
    }

    public IMessage getMessage() {
        return message;
    }

    public void setMessage(IMessage message) {
        this.message = message;
    }
    
}
