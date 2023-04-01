package me.kyllian.spigotconsole.files;

import lombok.Getter;
import me.kyllian.spigotconsole.SpigotConsolePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FlatFile {

    @Getter
    private SpigotConsolePlugin plugin;
    private final File file;

    @Getter
    private FileConfiguration fileConfiguration;

    public FlatFile(SpigotConsolePlugin plugin, String fileName) {
        this.plugin = plugin;

        if (!fileName.endsWith(".yml")) fileName += ".yml";

        file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                plugin.saveResource(fileName, false);
            } catch (Exception exception) {
                // Resource file doesn't exist, create a new one.
                try {
                    file.createNewFile();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

        reload();
    }

    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
