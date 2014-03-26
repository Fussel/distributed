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
import distributed.dto.IMessage;
import distributed.dto.PrivateMessage;
import distributed.dto.User;
import distributed.util.SettingsProvider;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.log4j.Logger;

/**
 *
 * @author steffen
 */
public class DatabaseManager {
    
    private static DatabaseManager instance;
    
    private static final Logger log = Logger.getLogger(DatabaseManager.class.getName());
    
    private Dao<User, String> userDao;
    private Dao<GroupMessage, UUID> postDao;
    private Dao<PrivateMessage, UUID> privateMessageDao;
    
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
       
            databasePath += SettingsProvider.getInstance().getDBDir();

            File dbPath = new File(databasePath);
            
            if(!dbPath.exists())
                dbPath.mkdirs();
            
            connection = new JdbcConnectionSource(DATABASE_DRIVER + SettingsProvider.getInstance().getCanonicalDatabaseFile());
            
            userDao = DaoManager.createDao(connection, User.class);
            postDao = DaoManager.createDao(connection, GroupMessage.class);
            privateMessageDao = DaoManager.createDao(connection, PrivateMessage.class);
            
            initDatabase();
            
        } catch(Exception e) {
            log.error("IN CONSTRUCTOR: " + e);
        }
    }
    
    private void initDatabase() {
        
        Class[] classes = {PrivateMessage.class, GroupMessage.class, User.class}; 
        
        try {
            for (Class tmpClass : classes) {
                TableUtils.createTableIfNotExists(connection, tmpClass);
            }
        
        } catch (SQLException sqlEx) {
            log.error("IN INITDATABASE: " + sqlEx);
        }
        
    } 
    
    public void insertPrivateMessage(PrivateMessage msg) {
        
        try {
            privateMessageDao.createIfNotExists(msg);
        } catch(SQLException sql) {
            log.error("Cannot add private message");
        }
    }
    
    public void insertPost(GroupMessage post) {
        
        try {
            System.out.println("insert");
            postDao.createIfNotExists(post);
        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
            log.error("IN INSERT: " + sqlEx);
        }     
    }
    
    /**
     * Loads all GroupMessages in the database and returns them 
     * in a list.
     * 
     * @return All GroupMessages in the database. 
     */
    public List<GroupMessage> loadPosts() {
    
        List<GroupMessage> posts = new ArrayList();
        
        try {
            posts = postDao.queryForAll();
        } catch (SQLException sqlEx) {
            log.error("IN LOAD: " + sqlEx);
        }
        
        return posts;       
    } 
    
    /**
     * Returns the count of all GroupMessage objects in the 
     * database. If an error occurs it returns -1.
     * 
     * @return The count of all GroupMessage objects, -1 if an 
     *      error occur
     */
    public long getGroupMessageCount() {
        try {
            return postDao.countOf();
        } catch(SQLException sql) {
            log.error(sql);
        }
        return -1;
    }
    
    /**
     * 
     * @return 
     */
    public long getPrivateMessageCount() {
        try {
            return privateMessageDao.countOf();
        } catch(SQLException sql) {
            log.error(sql);
        }
        return -1;
    }
    
    public List<GroupMessage> getAllSharedPosts() {
        try {
            return postDao.query(postDao.queryBuilder().where()
            .eq(IMessage.COL_NAME_SHARED, "1").prepare());
        } catch(SQLException sql) {
            log.error(sql);
        }
        
        return null;
    } 
    
    public List<PrivateMessage> getAllPrivateMessages() {
        try {
            return privateMessageDao.queryForAll();
        } catch(SQLException sql) {
            log.error(sql);
        }
        
        return null;
    }
    
    public List<PrivateMessage> getMyPrivateMessages() {
        List<PrivateMessage> msg = new ArrayList();
        //TODO
        return msg;
    }
    
    public void setPostAsShared(GroupMessage msg) {
        msg.setShared(true);
        
        try {
            postDao.update(msg);
        } catch(SQLException sql) {
            log.error(sql);
        }
    }
    
    public void markAsDeleted(GroupMessage msg) {
        msg.setDeleted(true);
        
        try {
            postDao.update(msg);
        } catch(SQLException sql) {
            log.error(sql);
        }
    }
    
    public void editMessage(GroupMessage msg, String newMessage) {
        msg.setMessage(newMessage.getBytes());
        
        try {
            postDao.update(msg);
        } catch(SQLException sql) {
            log.error(sql);
        }
    }
}
