package ru.primer.mc.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class Aes256Class {
    private SecretKey secretKey;
    public Aes256Class() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            this.secretKey = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public byte[] makeAes(byte[] rawMessage, int cipherMode){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMode, this.secretKey);
            byte [] output = cipher.doFinal(rawMessage);
            return output;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
