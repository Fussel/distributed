/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.update;

import distributed.database.DatabaseManager;
import static distributed.update.IncrementalUpdate.UpdateClient.log;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author steffen
 */
public class LeaderUpdateServer extends IncrementalUpdate.UpdateServer {

    public LeaderUpdateServer(int port) {
        super(port);
    }

    @Override
    protected void preparePrivateMessageList() {
        objectBuffer = new ArrayList<>();
        objectBuffer.addAll(DatabaseManager.getInstance().
                getAllPrivateMessages());
        Collections.sort(objectBuffer);
        log.debug("PrivateMessages load into objectBuffer");
    }

    @Override
    protected void prepareGroupMessageList() {
        objectBuffer = new ArrayList<>();
        objectBuffer.addAll(DatabaseManager.getInstance().
                getAllSharedPosts());
        log.debug("SharedGroupMessages load into objectBuffer");
    }  
}
