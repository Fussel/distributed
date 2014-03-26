/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.update;

/**
 *
 * @author steffen
 */
public class LeaderUpdateServer extends IncrementalUpdate.UpdateServer {

    public LeaderUpdateServer(int port) {
        super(port);
    }

    @Override
    protected void prepareGroupMessageList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void preparePrivateMessageList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
