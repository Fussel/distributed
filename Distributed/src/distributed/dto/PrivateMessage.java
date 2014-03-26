/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed.dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import distributed.util.SettingsProvider;
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
        super(SettingsProvider.getInstance().getUserName(), message);
        this.receiver = receiver;
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
    
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
