/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author steffen
 * @version 0.1 - not tested
 */
public class DistributedKrypto {
    private static DistributedKrypto instance;
    private static final String CIPHER_FORM     = "RSA/ECB/PKCS1Padding";
    private static final String ALGORITHM       = "RSA";
    private static final int KEY_LENGHT         = 1024;
    
    private final String PRIVATE_FILE;
    private final String PUBLIC_FILE;
    //TODO Auslagern des BaseDirs
    private final String DIR;
    
    private PrivateKey privateKey;
    private PublicKey publicKey;
    
    /**
     * Return an instance of DistributedKrypto.
     * @return Instance of DistributedKrypto.
     */
    public static synchronized DistributedKrypto getInstance() {
        if(instance == null)
            instance = new DistributedKrypto();
        return instance;
    }
    
    private DistributedKrypto() {
        File privateFile, publicFile;
        
        DIR = SettingsProvider.getInstance().getRootDir() + 
                SettingsProvider.getInstance().getKeyDir();
        
        PRIVATE_FILE = SettingsProvider.getInstance().getPrivateKeyName();
        PUBLIC_FILE = SettingsProvider.getInstance().getPublicKeyName();
        
        privateFile = new File(DIR + PRIVATE_FILE);
        publicFile = new File(DIR + PUBLIC_FILE);
        
        if(!publicFile.exists())
            generateKeys();
        else
            loadKeys();
    }
    
    /**
     * Generating a new key pair.
     * This method should only be called once, to ensure that 
     * the client is always using the same key.
     */
    private void generateKeys() {
        System.out.println("Generate Keys");
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM);
            gen.initialize(KEY_LENGHT);
            KeyPair pair;
            
            pair = gen.generateKeyPair();
            
            publicKey = pair.getPublic();
            privateKey = pair.getPrivate();
            storeKeys();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Store the keys in a file.
     */
    private void storeKeys() {
        System.out.println("Store keys");
        //Create dir
        File dir = new File(DIR);
        if(!dir.exists())
            dir.mkdirs();
        
        dir = null;
        //Store publickey
        X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(
                publicKey.getEncoded());
        try {
            FileOutputStream fos = new FileOutputStream(DIR + PUBLIC_FILE);
            fos.write(x509Spec.getEncoded());
            fos.close();
            fos = null;
            x509Spec = null;
        } catch(Exception e) {
            e.printStackTrace();
        } 
        
        //Store private key
        PKCS8EncodedKeySpec pkcsSpec = new PKCS8EncodedKeySpec(
                privateKey.getEncoded());
        
        try {
            FileOutputStream fos = new FileOutputStream(DIR + PRIVATE_FILE);
            fos.write(pkcsSpec.getEncoded());
            fos.close();
            fos = null;
            pkcsSpec = null;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Load the key file.
     */
    private void loadKeys() {
        System.out.println("Load keys");
        KeyFactory factory = null;
        
        try {
            factory = KeyFactory.getInstance(ALGORITHM);
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        //Read public key
        try {
            File publicFile = new File(DIR + PUBLIC_FILE);
            FileInputStream fis = new FileInputStream(DIR + PUBLIC_FILE);
            
            byte[] encoded = new byte[(int)publicFile.length()];
            fis.read(encoded);
            publicKey = factory.generatePublic(
                    new X509EncodedKeySpec(encoded));
            fis.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        //Read private key
        try {
            File privateFile = new File(DIR + PRIVATE_FILE);
            FileInputStream fis = new FileInputStream(privateFile);
            byte[] encoded = new byte[(int) privateFile.length()];
            fis.read(encoded);
            fis.close();
            privateKey = factory.generatePrivate(
                new PKCS8EncodedKeySpec(encoded));
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        factory = null;
    }
    
    /**
     * Get an instance of the local public key.
     * 
     * @return Instance of the local public key. 
     */
    public PublicKey getMyPublicKey() {
        return publicKey;
    }
    
    /**
     * Encrypt a string object with the given public key. The 
     * encryption is using the RSA algorithm with a 1024 key length.
     * 
     * The encrypted byte array <b>should not be modified, to prevent
     * invalid block segmentation</b>.
     * 
     * @param message The message which should be encrypted.
     * @param key The public key of the receiver.
     * @return 
     */
    public byte[] encryptString(String message, PublicKey key) {
        Cipher cipher;
        
        try {
            cipher = Cipher.getInstance(CIPHER_FORM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(message.getBytes());
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Decrypts an byte array with the own private key and returns the
     * string representation of it.
     * 
     * <b>The byte array must be unmodified to prevent invalid block 
     * segmentations</b>
     *
     * @param data The byte array which should be decrypted.
     * @return The decrypted string representation of the array.
     */
    public String decryptMessage(byte[] data) {
        try {
            Cipher cipher;
            cipher = Cipher.getInstance(CIPHER_FORM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(data));
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    } 
    
    /**
     * Easy access the string representation of the 
     * public key.
     * 
     * @return 
     */
    public String getPUblicKeyString() {
        return publicKeyToString(publicKey);
    }
    
    /**
     * Converts a public key to a string for easy storing.
     * 
     * @param key The public key which should be converted to a string.
     * @return The string representation of the public key.
     */
    public String publicKeyToString(PublicKey key) {
        byte[] daBytes;
        
        daBytes = key.getEncoded();
        BASE64Encoder encoder = new BASE64Encoder();
        
        return encoder.encode(daBytes);
    }
    
    /**
     * Converts a public key, which was decrypted as string
     * back to a public key;
     * 
     * If there were problems while decoding the method will 
     * return null.
     * 
     * @param keyBytes 
     * @return 
     */
    public PublicKey StringToPublicKey(String publicKey) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] daBytes;
        
        try {
            daBytes = decoder.decodeBuffer(publicKey);
            
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(daBytes);
            KeyFactory keyFac = KeyFactory.getInstance(ALGORITHM);
            return keyFac.generatePublic(keySpec);
            //TODO Add logging
        } catch(IOException io) {
            
        } catch(NoSuchAlgorithmException nsa) {
            
        } catch(InvalidKeySpecException iks) {
            
        }
        
        return null;
    }
}
