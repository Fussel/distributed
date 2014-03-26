/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.update;

import distributed.database.DatabaseManager;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author steffen
 */
public class LeaderUpdateClient extends IncrementalUpdate.UpdateClient {

    public LeaderUpdateClient(String ip, int port) {      
        super(ip, port);
        log.debug("LeaderupdateClient init");
    }

    @Override
    protected void loadPrivateMessages() {
        objectBuffer = new ArrayList<>();
        objectBuffer.addAll(DatabaseManager.getInstance().
                getAllPrivateMessages());
        Collections.sort(objectBuffer);
        log.debug("PrivateMessages load into objectBuffer");
    }

    @Override
    protected void loadGroupMessages() {
        objectBuffer = new ArrayList<>();
        objectBuffer.addAll(DatabaseManager.getInstance().
                getAllSharedPosts());
        log.debug("SharedGroupMessages load into objectBuffer");
    }
    
}
