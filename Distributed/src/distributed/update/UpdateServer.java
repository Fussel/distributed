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
public class UpdateServer extends AUpdateServer {

    public UpdateServer(int port) {
        super(port);
    }

    @Override
    protected void prepareGroupMessageList() {
            objectBuffer = new ArrayList<>();
            objectBuffer.addAll(DatabaseManager.getInstance().loadPosts());
            Collections.sort(objectBuffer);
            log.debug("Posts load into object buffer");
    }

    @Override
    protected void preparePrivateMessageList() {
            objectBuffer = new ArrayList<>();
            objectBuffer.addAll(DatabaseManager.getInstance().getAllPrivateMessages());
            Collections.sort(objectBuffer);
            log.debug("PrivateMessages added to objectBuffer");
        }
    
}
