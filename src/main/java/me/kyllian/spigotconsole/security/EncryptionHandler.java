package me.kyllian.spigotconsole.security;

import me.kyllian.spigotconsole.SpigotConsolePlugin;
import me.kyllian.spigotconsole.models.AppUser;
import org.bukkit.entity.Player;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyFactory;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionHandler {

    private final SpigotConsolePlugin plugin;

    public EncryptionHandler(SpigotConsolePlugin plugin) {
        this.plugin = plugin;
    }

    public Key generateKey(Player player) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256, secureRandom);

            return keyGenerator.generateKey();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public String encrypt(String string, AppUser appUser) {
        try {
            Key key = appUser.getKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(string.getBytes("UTF-8")));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public String decrypt(String string, AppUser appUser) {
        try {
            Key key = appUser.getKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(string)));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
