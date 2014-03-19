/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author ygraf
 */
@DatabaseTable(tableName = "user")
public class User {
    
    
    public static final String COL_NAME_ID      = "id";
    public static final String COL_NAME_NAME    = "name";
    
    @DatabaseField(columnName = COL_NAME_ID, id = true)
    private String userID;
    @DatabaseField(columnName = COL_NAME_NAME)
    private String userName;
    
    
    public User() {
        //needed by ORMLite
    }
    
    public User(String userID, String userName) {
        this.userID   = userID;
        this.userName = userName;
    }

    
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
}
