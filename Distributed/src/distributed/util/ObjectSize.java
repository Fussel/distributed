/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.util;

//TODO This class is needed to build our own message header. 

import java.lang.instrument.Instrumentation;

/**
 * This class determines the size of an Object.
 * 
 * @author steffen
 */
public class ObjectSize {
    
    private static ObjectSize instance;
    private Instrumentation instrumentation;
    
    public ObjectSize getInstance() {
        if(instance == null)
            instance = new ObjectSize();
        
        return instance;
    } 
    
    private ObjectSize() {
        //TODO instrumentation must be fetched in an premain defined in the manifest 
    }
}
