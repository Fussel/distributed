/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.update;

import java.io.Serializable;

/**
 *
 * @author steffen
 */
public class UpdateProtokoll implements Serializable {
    
    public static enum UpdateTask {
        LOAD_GROUP_MESSAGES, 
        LOAD_PRIVATE_MESSAGES
    }
    
    private UpdateTask task;
    
    private String hashValue;
    private int objectCount;
    private int objectOffset;
    
    public UpdateProtokoll(UpdateTask task, int objectCount) {
        this.task = task;
        this.objectCount = objectCount;
    }
    
    public UpdateProtokoll.UpdateTask getTask() {
        return this.task;
    }
    
    public int getObjectCount() {
        return objectCount;
    }
    
}
