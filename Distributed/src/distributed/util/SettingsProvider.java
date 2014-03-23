/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Class to store and load system settings.
 * 
 * @author steffen
 */
public class SettingsProvider {
    
    private static SettingsProvider instance;
    private  Properties props;
    
    private static final String C_DB_NAME       = "/distributed.sqlite";
    private static final String C_KEY_DIR       = "/keys";
    private static final String C_DB_DIR        = "/db";
    private static final String C_PUB_KEY       = "/pub.key";
    private static final String C_PRI_KEY       = "/priv.key";
    
    //TODO Add all needed property tags
    private static final String USER_NAME       = "user.name";
    private static final String USER_GROUP      = "user.group";
    private static final String USER_TYPE       = "user.type";
    private static final String ROOT_DIR        = "dir.root";
    private static final String KEY_DIR         = "dir.key";
    private static final String PUB_KEY         = "key.public";
    private static final String PRI_KEY         = "key.private";
    private static final String DB_DIR          = "dir.database";
    private static final String DB_NAME         = "db.name";
    
    public static enum UserType {
        MODERATOR, USER;
    }
    
    private static final String SETTINGS_FILE = "../Settings.properties";
   
    public synchronized static SettingsProvider getInstance() {
        if(instance == null) {
           instance = new SettingsProvider();
        }
        
        return instance;
    }
    
    private SettingsProvider() {
        try {
            File settingsFile = new File(SETTINGS_FILE);
            settingsFile.createNewFile();
            props = new Properties();
            BufferedInputStream in = new BufferedInputStream(
                new FileInputStream(settingsFile));
            props.load(in);
            in.close();
            
            checkValues();
        } catch(IOException e) {
            e.printStackTrace();
        } 
    }
    
    private void checkValues() {
        if(props.getProperty(ROOT_DIR) == null)
            props.setProperty(ROOT_DIR, System.getProperty("user.home"));
        
        if(props.getProperty(DB_DIR) == null)
            props.setProperty(DB_DIR, C_DB_DIR);
            
        if(props.getProperty(DB_NAME) == null)
            props.setProperty(DB_NAME, C_DB_NAME);
        
        if(props.getProperty(KEY_DIR) == null)
            props.setProperty(KEY_DIR, C_KEY_DIR);
        
        writePropertyChanges();
    }
    
    public void storeUserName(String userName) {
        props.setProperty(USER_NAME, userName);
        writePropertyChanges();
    }
    
    public void storeUserGroup(String userGroup) {
        props.setProperty(USER_GROUP, userGroup);
        writePropertyChanges();
    }
    
    /**
     * The type of the user. At the moment just moderator or user
     * are allowed;
     * 
     * @param userType 
     */
    public void storeUserType(UserType userType) {
        props.setProperty(USER_TYPE, userType.name());
        writePropertyChanges();
    }
    
    /**
     * 
     */
    public void storeRootDirectory(String rootDirectory) {
        //check if the root dir is already set. if so all files must be copied.
        String path = (String) props.get(ROOT_DIR);
        if(path == null || path.isEmpty()){
            props.setProperty(ROOT_DIR, rootDirectory);
            writePropertyChanges();
        }
    }
       
    public String getUserName() {
        return props.getProperty(USER_NAME, null);
    }
    
    public String getUserGroup() {
        return props.getProperty(USER_GROUP, null);
    }
    
    public UserType getUserType() {
        //TODO change the enum to set up with the string parameter
        String type = props.getProperty(USER_TYPE, null);
        
        if(type == null) 
            return null; //TODO Or should client be returned?
        else if(type.equals("MODERATOR"))
            return UserType.MODERATOR;
        else if(type.equals("USER"))
            return UserType.USER;
        
        //This shall be never reached
        return null;
    }
    
    public String getRootDir() {
        return props.getProperty(ROOT_DIR, null);
    }
    
    public String getDBDir() {
        return props.getProperty(DB_DIR, null);
    }
    
    public String getKeyDir() {
        return props.getProperty(KEY_DIR, null);
    }
    
    public String getPublicKeyName() {
        return props.getProperty(PUB_KEY, null);
    }
    
    public String getPrivateKeyDir() {
        return props.getProperty(PRI_KEY, null);
    }
    
    public String getDatabaseName() {
        return props.getProperty(DB_NAME, null);
    }
    
    public String getCanonicalDatabaseFile() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(props.getProperty(ROOT_DIR));
        builder.append(props.getProperty(DB_DIR));
        builder.append(props.getProperty(DB_NAME));
        
        return builder.toString();
        //Maybe null builder for gc
    }
    
    private boolean writePropertyChanges() {
        try {
            File props = new File(SETTINGS_FILE);
            OutputStream stream = new FileOutputStream(props);
            this.props.store(stream, "Settings Change");
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
