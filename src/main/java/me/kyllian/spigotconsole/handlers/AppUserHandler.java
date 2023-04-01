package me.kyllian.spigotconsole.handlers;

import me.kyllian.spigotconsole.SpigotConsolePlugin;
import me.kyllian.spigotconsole.models.AppUser;
import me.kyllian.spigotconsole.models.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

public class AppUserHandler {

    private SpigotConsolePlugin plugin;
    private List<AppUser> appUsers;

    public AppUserHandler(SpigotConsolePlugin plugin) {
        this.plugin = plugin;

        appUsers = new ArrayList<>();

        load();
    }

    public void load() {
        for (String uuid : plugin.getKeyFile().getFileConfiguration().getKeys(false)) {
            UUID parsedUUID = UUID.fromString(uuid);

            // The key is a Base64 encoded string

            String key = plugin.getKeyFile().getFileConfiguration().getString(uuid + ".key");
            String decodedKey = Arrays.toString(Base64.getDecoder().decode(key));
            Bukkit.getLogger().info("Decoded key: " + decodedKey);
            Key parsedKey = new SecretKeySpec(decodedKey.getBytes(), 0, decodedKey.length(), "AES");

            String role = plugin.getKeyFile().getFileConfiguration().getString(uuid + ".role");

            Role foundRole = plugin.getRoleHandler().getRoleByName(role);

            appUsers.add(new AppUser(parsedUUID, parsedKey, foundRole));
        }
    }

    public AppUser generateAppUser(Player player) {
        UUID uuid = player.getUniqueId();
        Key key = plugin.getEncryptionHandler().generateKey(player);
        Role role = plugin.getRoleHandler().getHighestRole(player);

        addAppUser(uuid, role, key);

        return new AppUser(uuid, key, role);
    }

    public void addAppUser(UUID uuid, Role role, Key key) {
        appUsers.add(new AppUser(uuid, key, role));

        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        plugin.getKeyFile().getFileConfiguration().set(uuid.toString() + ".key", encodedKey);
        plugin.getKeyFile().getFileConfiguration().set(uuid.toString() + ".role", role.getName());
        plugin.getKeyFile().save();
    }

    public void removeAppUser(UUID uuid) {
        AppUser appUser = getAppUser(uuid);
        if (appUser == null) return;

        appUsers.remove(appUser);
        plugin.getKeyFile().getFileConfiguration().set(uuid.toString(), null);
        plugin.getKeyFile().save();
    }

    public void confirm(AppUser appUser) {
        appUser.setConfirmed(true);
        plugin.getKeyFile().getFileConfiguration().set(appUser.getUuid().toString() + ".confirmed", true);
        plugin.getKeyFile().save();
    }

    public AppUser getAppUser(UUID uuid) {
        return appUsers.stream().filter(appUser -> appUser.getUuid().equals(uuid)).findFirst().orElse(null);
    }
}
