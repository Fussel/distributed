/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Objects;
import sun.misc.BASE64Encoder;

/**
 *
 * @author ygraf
 */
@DatabaseTable(tableName = "user")
public class User {
    
    //Id will be the string representation of the public key of the user
    public static final String COL_NAME_ID      = "id";
    public static final String COL_NAME_NAME    = "name";
    
    @DatabaseField(columnName = COL_NAME_ID, id = true)
    private byte[] userID;
    @DatabaseField(columnName = COL_NAME_NAME)
    private String userName;
    
    
    public User() {
        //needed by ORMLite
    }
    
    public User(String userID, String userName) {
        this.userID   = userID.getBytes();
        this.userName = userName;
    }

    
    public String getUserID() {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(userID);
    }

    public void setUserID(String userID) {
        this.userID = userID.getBytes();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.userID);
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
        final User other = (User) obj;
        if (!Objects.equals(this.userID, other.userID)) {
            return false;
        }
        return true;
    }
}
