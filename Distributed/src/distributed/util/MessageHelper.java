/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.util;

import distributed.dto.GroupMessage;
import distributed.dto.PrivateMessage;
import java.security.PublicKey;
import org.jgroups.Message;

/**
 * Helper class to work with message objects.
 * 
 * @author steffen
 * @version 0.1 - not tested
 */
public class MessageHelper {

    /**
     * Helper to create an encrypted message. This function carries about 
     * the message building and the string encryption.
     * 
     * @param post The original post object, to which should be answered.
     * @param msg The message as a String representation. The string will be encrypted 
     *  with the public key of the post object.
     * @return 
     */
    public static Message buildPrivateMessage(GroupMessage post, String msg) {
        PublicKey key;
        
        key = DistributedKrypto.getInstance().StringToPublicKey(post.getKey());
        
        byte[] encodedMessage = DistributedKrypto.getInstance().encryptString(msg, key);
        PrivateMessage message = new PrivateMessage(post.getSender(), encodedMessage);
        
        return new Message(null, message);
    }
}
