package com.accommodation.system.utils2;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
public class AESUtil {
    private static final String key = "ae1kncryptionKey";
    private static final String initVector = "encryptionIntVec";
    private static IvParameterSpec iv;
    private static SecretKeySpec skeySpec;
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;
    static {
        try {
            iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            encryptCipher=cipher;
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            decryptCipher=cipher;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String encrypt(String value) {
        try {
            encryptCipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = encryptCipher.doFinal(value.getBytes());
            return Base64.getEncoder().withoutPadding().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encrypted) {
        try {
            decryptCipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = decryptCipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
