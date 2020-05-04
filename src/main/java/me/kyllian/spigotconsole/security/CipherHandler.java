package me.kyllian.spigotconsole.security;

import javafx.print.PageLayout;
import me.kyllian.spigotconsole.SpigotConsolePlugin;
import org.bukkit.entity.Player;

import javax.crypto.Cipher;
import java.security.Key;
import java.util.Base64;
import java.util.UUID;

public class CipherHandler {

    private SpigotConsolePlugin plugin;

    public CipherHandler(SpigotConsolePlugin plugin) {
        this.plugin = plugin;
    }

    public String encrypt(String toEncrypt, UUID uuid) {
        try {
            Key key = KeyFactory.getKey(uuid, plugin);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(toEncrypt.getBytes("UTF-8")));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public String decrypt(String toDecrypt, UUID uuid) {
        try {
            Key key = KeyFactory.getKey(uuid, plugin);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(toDecrypt)));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Key getOrCreateKey(Player player) {
        return KeyFactory.getKey(player.getUniqueId(), plugin);
    }
}
