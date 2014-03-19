/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.test;

import distributed.database.DatabaseManager;
import distributed.dto.GroupMessage;
import distributed.util.DistributedKrypto;
import java.util.List;

/**
 *
 * @author ygraf
 */
public class DatabaseTest {
    
    
    public static void main(String[] args) {
    
        GroupMessage tmpPost1 = new GroupMessage("Test Message 1", DistributedKrypto.getInstance().getMyPublicKey());
        GroupMessage tmpPost2 = new GroupMessage("Test Message 2", DistributedKrypto.getInstance().getMyPublicKey());
        GroupMessage tmpPost3 = new GroupMessage("Test Message 3", DistributedKrypto.getInstance().getMyPublicKey());
    
        DatabaseManager manager = DatabaseManager.getInstance();
        
        manager.insertPost(tmpPost1);
        manager.insertPost(tmpPost2);
        manager.insertPost(tmpPost3);
        
        List<GroupMessage> posts = manager.loadPosts();
        
        for (GroupMessage post : posts) {
            System.out.println(post);
        }
        
    }
    
}
