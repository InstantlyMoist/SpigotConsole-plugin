package me.kyllian.spigotconsole;

import me.kyllian.spigotconsole.commands.SpigotConsoleExecutor;
import me.kyllian.spigotconsole.handlers.PlayerDataHandler;
import me.kyllian.spigotconsole.handlers.map.MapHandler;
import me.kyllian.spigotconsole.handlers.map.MapHandlerFactory;
import me.kyllian.spigotconsole.security.CipherHandler;
import me.kyllian.spigotconsole.storage.KeyFileHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotConsolePlugin extends JavaPlugin {

    private CipherHandler cipherHandler;
    private MapHandler mapHandler;
    private PlayerDataHandler playerDataHandler;
    private KeyFileHandler keyFileHandler;

    @Override
    public void onEnable() {
        super.onEnable();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();


        getCommand("spigotconsole").setExecutor(new SpigotConsoleExecutor(this));

        cipherHandler = new CipherHandler(this);
        mapHandler = new MapHandlerFactory(this).getMapHandler();
        playerDataHandler = new PlayerDataHandler();
        keyFileHandler = new KeyFileHandler(this);
    }

    public CipherHandler getCipherHandler() {
        return cipherHandler;
    }

    public MapHandler getMapHandler() {
        return mapHandler;
    }

    public PlayerDataHandler getPlayerDataHandler() {
        return playerDataHandler;
    }

    public KeyFileHandler getKeyFileHandler() {
        return keyFileHandler;
    }


}
