/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import distributed.dto.GroupMessage;
import distributed.dto.PrivateMessage;
import distributed.dto.User;
import distributed.util.SettingsProvider;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author steffen
 */
public class DatabaseManager {
    
    private static DatabaseManager instance;
    
    private Dao<User, String> userDao;
    private Dao<GroupMessage, Integer> postDao;
    private Dao<PrivateMessage, Integer> privateMessageDao;
    
    private ConnectionSource connection;
    private static final String DATABASE_DRIVER = "jdbc:sqlite:";
    
    public synchronized static DatabaseManager getInstance() {
        if(instance == null)
            instance = new DatabaseManager();
        
        return instance;
    }
    
    private DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            
            String databasePath = SettingsProvider.getInstance().getRootDir();
            databasePath = "/Users/ygraf/test.db";//databasePath.concat(SettingsProvider.getInstance().getDBDir());
            
            //File dbPath = new File(databasePath);
            //if(!dbPath.exists())
             //   dbPath.mkdir();
            
            connection = new JdbcConnectionSource(DATABASE_DRIVER + databasePath);
            
            userDao = DaoManager.createDao(connection, User.class);
            postDao = DaoManager.createDao(connection, GroupMessage.class);
            privateMessageDao = DaoManager.createDao(connection, PrivateMessage.class);
            
            initDatabase();
            
        } catch(Exception e) {
            System.out.println("IN CONSTRUCTOR: " + e);
        }
    }
    
    private void initDatabase() {
        
        Class[] classes = {PrivateMessage.class, GroupMessage.class, User.class}; 
        
        try {
        
            for (Class tmpClass : classes) {
                System.out.println("KNOCK KNOCK");
                TableUtils.createTableIfNotExists(connection, tmpClass);
            }
        
        } catch (SQLException sqlEx) {
            System.out.println("IN INITDATABASE: " + sqlEx);
        }
        
    } 
    
    
    public boolean insertPost(GroupMessage post) {
        
        boolean result = false;
        
        try {
            postDao.createIfNotExists(post);
        } catch (SQLException sqlEx) {
           System.out.println("IN INSERT: " + sqlEx);
        }
        
        return result;
        
    }
    
    public List<GroupMessage> loadPosts() {
    
        List<GroupMessage> posts = new ArrayList();
        
        try {
            posts = postDao.queryForAll();
        } catch (SQLException sqlEx) {
            System.out.println("IN LOAD: " + sqlEx);
        }
        
        return posts;
        
    }
    
    
    
    
    
}
