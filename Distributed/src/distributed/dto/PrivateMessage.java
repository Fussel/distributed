/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed.dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author steffen
 */
@DatabaseTable(tableName = "private_message")
public class PrivateMessage extends IMessage implements Serializable {

    public static final String TABLE_NAME       = "private_message";
    public static final String COL_NAME_RECIVER = "recivier";
    
    @DatabaseField(columnName = COL_NAME_RECIVER)
    private String receiver;

    public PrivateMessage() {
        //Needed by ORMLite
    }

    public PrivateMessage(String receiver, byte[] message) {
        super("", message);
        this.receiver = receiver;
        //TODO Set sender Prop
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        
        if(!super.equals(obj))
            return false;
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final PrivateMessage other = (PrivateMessage) obj;
        
        if (!Objects.equals(this.receiver, other.receiver)) {
            return false;
        }
        
        return true;
    }

    
}
