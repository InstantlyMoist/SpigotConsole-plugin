package me.kyllian.spigotconsole;

import me.kyllian.spigotconsole.commands.SpigotConsoleExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotConsolePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        getCommand("spigotconsole").setExecutor(new SpigotConsoleExecutor());
    }
}
