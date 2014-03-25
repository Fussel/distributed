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
public interface IDistributedUpdate {
    
    public static interface Server extends Runnable {}
    public static interface Client extends Runnable {}
}
