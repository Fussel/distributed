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
public class UpdateClient extends IncrementalUpdate.UpdateClient {

    public UpdateClient(String ip, int port) {
        super(ip, port);
    }
    
}
