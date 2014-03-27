/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.util;

import distributed.dto.User;
import java.io.IOException;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author steffen
 */
public class Util {
    
    private static final Logger log = Logger.getLogger(Util.class);
    
    /**
     * Returns the user object.
     * 
     * @return 
     */
    public static User createMyUser() {
        return new User(DistributedKrypto.getInstance().getPUblicKeyString(), 
            SettingsProvider.getInstance().getUserName());
    }
    
    public static byte[] BASE64D (String string) {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            return decoder.decodeBuffer(string);
        } catch(IOException io) {
            log.error(io);
            return null;
        }
    }
    
    public static String BASE64E(byte[] bar) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bar);
    }
    
}
