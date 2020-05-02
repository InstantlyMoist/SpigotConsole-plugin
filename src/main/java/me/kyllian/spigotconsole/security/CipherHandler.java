package me.kyllian.spigotconsole.security;

import javax.crypto.Cipher;
import javax.swing.plaf.basic.BasicTreeUI;
import java.security.Key;
import java.util.Base64;
import java.util.UUID;

public class CipherHandler {

    public static String encrypt(String toEncrypt, UUID uuid) {
        try {
            Key key = KeyFactory.getKey(uuid);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(toEncrypt.getBytes("UTF-8")));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String toDecrypt, UUID uuid) {
        try {
            Key key = KeyFactory.getKey(uuid);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(toDecrypt)));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
