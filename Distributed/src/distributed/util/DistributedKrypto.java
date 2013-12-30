/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

/**
 *
 * @author steffen
 */
public class DistributedKrypto {
    private static DistributedKrypto instance;
    private static final String CIPHER_FORM = "RSA/ECB/PKCS1Padding";
    private static final String ALGORITHM = "RSA";
    private static final int KEY_LENGHT = 1024;
    
    private static final String PRIVATE_FILE = "private.key";
    private static final String PUBLIC_FILE = "public.key";
    //TODO Auslagern des BaseDirs
    private static final String DIR = System.getProperty("user.home") +
            System.getProperty("file.separator") + "Distributed" 
            + System.getProperty("file.separator");
    
    private PrivateKey privateKey;
    private PublicKey publicKey;
    
    public static synchronized DistributedKrypto getInstance() {
        if(instance == null)
            instance = new DistributedKrypto();
        return instance;
    }
    
    private DistributedKrypto() {
        File privateFile, publicFile;
        
        privateFile = new File(DIR + PRIVATE_FILE);
        publicFile = new File(DIR + PUBLIC_FILE);
        
        if(!publicFile.exists())
            generateKeys();
        else
            loadKeys();
    }
    
    private void generateKeys() {
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
    
    private void storeKeys() {
        //Create dir
        File dir = new File(DIR);
        if(!dir.exists())
            dir.mkdir();
        
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
    
    private void loadKeys() {
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
    
    public PublicKey getMyPublicKey() {
        return publicKey;
    }
    
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
    
    public String decryptMessage(byte[] data) {
        try {
            Cipher cipher;
            cipher = Cipher.getInstance(CIPHER_FORM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal());
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }  
}
