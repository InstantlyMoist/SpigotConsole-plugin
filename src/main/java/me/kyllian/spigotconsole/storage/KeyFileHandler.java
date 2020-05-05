package me.kyllian.spigotconsole.storage;

import me.kyllian.spigotconsole.SpigotConsolePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.UUID;

public class KeyFileHandler {

    private SpigotConsolePlugin plugin;

    private File file;
    private FileConfiguration fileConfiguration;

    public KeyFileHandler(SpigotConsolePlugin plugin) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), "keys.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
                //TODO: Stop server nicely
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
            // TODO: Retry later?
        }
    }

    public void setKey(UUID uuid, String rawKey) {
        fileConfiguration.set(uuid.toString(), rawKey);
        save();
    }

    public String getKey(UUID uuid) {
        return fileConfiguration.getString(uuid.toString());
    }

    public boolean keyExists(String key) {
        boolean exists = false;
        for (String uuid : fileConfiguration.getConfigurationSection("").getKeys(false)) {
            if (fileConfiguration.getString(uuid).equalsIgnoreCase(key)) exists = true;
        }
        return exists;
    }
}
