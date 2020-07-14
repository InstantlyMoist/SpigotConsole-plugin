package me.kyllian.spigotconsole.security;

import me.kyllian.spigotconsole.SpigotConsolePlugin;
import org.bukkit.Bukkit;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class KeyFactory {

    public static Key generateKey(UUID uuid, SpigotConsolePlugin plugin) {
        try {
            SecureRandom random = new SecureRandom();
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256, random);
            SecretKey generatedKey = generator.generateKey();
            String encodedKey = Base64.getEncoder().encodeToString(generatedKey.getEncoded());
            Bukkit.broadcastMessage(encodedKey);
            plugin.getKeyFileHandler().setKey(uuid, encodedKey);
            return generatedKey;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Key getKey(UUID uuid, SpigotConsolePlugin plugin) {
        String foundKey = plugin.getKeyFileHandler().getKey(uuid);
        if (foundKey == null) {
            generateKey(uuid, plugin);
            return getKey(uuid, plugin);
        }
        byte[] decodedKey = Base64.getDecoder().decode(foundKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}
