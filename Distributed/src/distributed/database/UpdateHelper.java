/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.database;

/**
 *
 * @author steffen
 */
public class UpdateHelper {
    
    public void generateObjectHashAll(HasherCallback callback) {
        
    }
    
    public void generateObjectHash(HasherCallback callback, int  range) {
        
    }
    
    public void getObjectCount(SizeCallback callback) {
        
    }
    
    public static interface HasherCallback {
        void hashCalculated(String hash);
    }
    
    public static interface SizeCallback {
        void countCalculated();
    }
}
