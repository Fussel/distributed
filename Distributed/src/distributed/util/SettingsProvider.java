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
import java.io.FileWriter;
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
    
    //TODO Add all needed property tags
    private static final String USER_NAME = "user.name";
    private static final String USER_GROUP = "user.group";
    private static final String USER_TYPE    = "user.type";
    private static final String ROOT_DIR = "dir.root";
    private static final String KEY_DIR = "dir.key";
    private static final String PUB_KEY = "key.public";
    private static final String PRI_KEY = "key.private";
    private static final String DB_DIR  = "dir.database";
    private static final String DB_NAME = "db.name";
    
    public static enum UserType {
        MODERATOR, USER;
    }
    
    private static final String SETTINGS_FILE = "../Settings.properties";
   
    public synchronized static SettingsProvider getInstance() {
        if(instance == null) 
           instance = new SettingsProvider();
        
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
        } catch(IOException e) {
            e.printStackTrace();
        } 
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
    public void setRootDirectory(String rootDirectory) {
        //TODO check if the root dir is already set. if so all files must be copied.
        
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
