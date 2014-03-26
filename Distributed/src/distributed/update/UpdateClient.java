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
public class UpdateClient extends IncrementalUpdate.UpdateClient {

    public UpdateClient(String ip, int port) {
        super(ip, port);
    }

    @Override
    protected void loadPrivateMessages() {
            objectBuffer = new ArrayList<>();
            objectBuffer.addAll(DatabaseManager.getInstance().getAllPrivateMessages());
            Collections.sort(objectBuffer);
            log.debug("PrivateMessages load into objectBuffer");
        }

    @Override
    protected void loadGroupMessages() {
            objectBuffer = new ArrayList<>();
            objectBuffer.addAll(DatabaseManager.getInstance().loadPosts());
            Collections.sort(objectBuffer);
            log.debug("GroupMessages load into objectBuffer");
        }
    
}
