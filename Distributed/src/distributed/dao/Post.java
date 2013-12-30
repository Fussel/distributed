/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.dao;

import java.security.PublicKey;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author steffen
 */
public class Post {
    
    private PublicKey key;
    private Date postDate;
    private String sender;
    private String message;
    
    public Post(String message, PublicKey key) {
        this.key = key;
        this.message = message;
        postDate = GregorianCalendar.getInstance().getTime();
        //TODO Set sender prop
    }

    public PublicKey getKey() {
        return key;
    }

    public Date getPostDate() {
        return postDate;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
