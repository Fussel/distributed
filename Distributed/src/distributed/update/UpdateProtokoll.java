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
        LOAD_PRIVATE_MESSAGES,
        START_UPDATE,
        SUCESSFUL, 
        COMPARE_IMSG_HASH,
        HASH_EQUALS,
        HASH_DIFFERS,
        FAILURE
    }
    
    private UpdateTask task;
    
    private int hash;
    private int objectCount;
    private int pivot;
    
    public UpdateProtokoll(UpdateTask task) {
        this.task = task;
    }
    
    public UpdateProtokoll(UpdateTask task, int pivot, int objectCount, int hash) {
        this.task           = task;
        this.pivot          = pivot;
        this.objectCount    = objectCount;
        this.hash           = hash;
    }
    
    public UpdateProtokoll(UpdateTask task,int pivot, int objectCount) {
        this.task           = task;
        this.pivot          = pivot;
        this.objectCount    = objectCount;
    }
    
    public UpdateProtokoll(UpdateTask task, int objectCount) {
        this.task           = task;
        this.objectCount    = objectCount; 
    }
    
    public UpdateProtokoll.UpdateTask getTask() {
        return this.task;
    }
    
    public int getObjectCount() {
        return objectCount;
    }
    
    public int getPivot() {
        return pivot;
    }
    
    public int getHash() {
        return hash;
    }
    
}
