/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.util;

import distributed.dto.User;

/**
 *
 * @author steffen
 */
public class Util {
    
    /**
     * Returns the user object.
     * 
     * @return 
     */
    public static User createMyUser() {
        return new User(DistributedKrypto.getInstance().getPUblicKeyString(), 
            SettingsProvider.getInstance().getUserName());
    }
    
}
