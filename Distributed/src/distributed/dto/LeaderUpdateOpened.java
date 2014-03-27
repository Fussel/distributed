/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.dto;

/**
 *
 * @author steffen
 */
public class LeaderUpdateOpened {
    public final String ip;
    public final int port;
    
    public LeaderUpdateOpened(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
