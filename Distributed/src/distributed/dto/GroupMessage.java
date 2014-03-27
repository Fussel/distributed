/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.dto;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import distributed.util.SettingsProvider;
import distributed.util.Util;
import java.io.Serializable;
import java.security.PublicKey;

/**
 *
 * @author steffen
 */
@DatabaseTable(tableName = "group_message")
public class GroupMessage extends IMessage implements Serializable {
    
    public static final String TABLE_NAME           = "group_message";
    public final static String COL_NAME_PUBLIC_KEY  = "public_key";
    

    @DatabaseField(columnName = COL_NAME_PUBLIC_KEY, dataType = DataType.BYTE_ARRAY)
    private byte[] key;


    public GroupMessage() {
        //needed by ORMLite
    }
        
    public GroupMessage(String key, String message) {
        super(SettingsProvider.getInstance().getUserName(), message.getBytes());
        this.key = Util.BASE64D(key);
    }

    public String getKey() {
        return Util.BASE64E(key);
    }
    
    public String getMessageString() {
        return new String(message);
    }
    
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
