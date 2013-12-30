/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.main;

import distributed.net.DistributedCore;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.jgroups.Message;

/**
 *
 * @author steffen
 */
public class TestClass {
    
    public static void main(String[] args) {
        List<CoreThreader> coreList = new ArrayList<>();
        int rand;
        
        System.out.println("Creating instances!");
        
        for(int i = 0; i < 10; i++) {
            CoreThreader t = new CoreThreader();
            t.start();
            
            coreList.add(t);
        }
        
        
        while(true) {
           
          
        }
    }
    
    public static class CoreThreader extends Thread {
        
        private DistributedCore core;
        
        public void run() {
            System.out.println("Init core");
            
            core = DistributedCore.getInstance();
            core.joinGroup("Test1");
            
            while(true) {
                
               // core.sendMessage(new Message().setObject(Math.random() * 10));
                
                try {
                    Thread.sleep((long)Math.random() * 100);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
