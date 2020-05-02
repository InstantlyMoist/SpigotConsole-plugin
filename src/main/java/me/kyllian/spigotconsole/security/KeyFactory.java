package me.kyllian.spigotconsole.security;

import org.bukkit.Bukkit;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.UUID;

public class KeyFactory {

    static HashMap<UUID, Key> keys = new HashMap<>(); //TEMP FOR TESTING PURPOSES

    public static Key generateKey(UUID uuid) {
        Bukkit.broadcastMessage("Key doens't exist, creating");
        try {
            SecureRandom random = new SecureRandom();
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256, random);
            return generator.generateKey();
        } catch (Exception exception) {
            exception.printStackTrace();
            //TODO: Handle this?
        }
        return null;
    }

    public static Key getKey(UUID uuid) {
        return keys.computeIfAbsent(uuid, f -> generateKey(uuid));
        //TODO Later change to SQL/YAML data storage.
    }
}
