package me.kyllian.spigotconsole.files;

import me.kyllian.spigotconsole.SpigotConsolePlugin;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

public class LogFile {

    private final String LATEST_FILE_PATH = "/logs/latest.txt";
    private final SpigotConsolePlugin plugin;
    private final File file;

    public LogFile(SpigotConsolePlugin plugin) {
        this.plugin = plugin;

        moveLatest();

        file = new File(plugin.getDataFolder(), "/logs/latest.txt");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void moveLatest() {
        File latestFile = new File(plugin.getDataFolder(), LATEST_FILE_PATH);
        if (!latestFile.exists()) {
            Bukkit.getLogger().info("Latest file doesn't exist, creating a new one.");
            return;
        }
        File newFile = new File(plugin.getDataFolder(), "/logs/" + System.currentTimeMillis() + ".txt");
        latestFile.renameTo(newFile);
        Bukkit.getLogger().info("Moved latest file to " + newFile.getName() + "");
    }

    public void log(UUID uuid, String message) {
        // Write to file
        log("[" + uuid.toString() + "] " + message);
    }

    public void log(String message) {
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(message);
            fileWriter.write(System.lineSeparator());
            fileWriter.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
