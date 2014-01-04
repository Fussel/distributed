/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import distributed.util.SettingsProvider;
import java.io.File;

/**
 *
 * @author steffen
 */
public class DatabaseManager {
    
    private static DatabaseManager instance;
    
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
            databasePath = databasePath.concat(SettingsProvider.getInstance().getDBDir());
            
            File dbPath = new File(databasePath);
            if(!dbPath.exists())
                dbPath.mkdir();
            
            connection = new JdbcConnectionSource(DATABASE_DRIVER + 
                    SettingsProvider.getInstance().getCanonicalDatabaseFile());
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
