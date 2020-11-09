package me.kyllian.spigotconsole;

import me.kyllian.spigotconsole.commands.SpigotConsoleExecutor;
import me.kyllian.spigotconsole.handlers.ConnectionHandler;
import me.kyllian.spigotconsole.handlers.ConsoleHandler;
import me.kyllian.spigotconsole.handlers.PlayerDataHandler;
import me.kyllian.spigotconsole.handlers.map.MapHandler;
import me.kyllian.spigotconsole.handlers.map.MapHandlerFactory;
import me.kyllian.spigotconsole.security.CipherHandler;
import me.kyllian.spigotconsole.storage.KeyFileHandler;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import spark.Spark;

import java.util.concurrent.Callable;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static spark.Spark.staticFileLocation;
import static spark.Spark.webSocket;

public class SpigotConsolePlugin extends JavaPlugin {

    private ConnectionHandler connectionHandler;
    private ConsoleHandler consoleHandler;
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

        staticFileLocation("/public");
        connectionHandler = new ConnectionHandler(this);
        webSocket("/spigotconsole", connectionHandler);

        Spark.port(getConfig().getInt("settings.port"));
        Spark.init();

        consoleHandler = new ConsoleHandler(this);
        Logger log = (Logger) LogManager.getRootLogger();
        log.addAppender(consoleHandler);

        cipherHandler = new CipherHandler(this);
        mapHandler = new MapHandlerFactory(this).getMapHandler();
        playerDataHandler = new PlayerDataHandler();
        keyFileHandler = new KeyFileHandler(this);

        Metrics metrics = new Metrics(this, 8201);

        metrics.addCustomChart(new Metrics.SingleLineChart("used_devices", () -> keyFileHandler.getKeyAmount()));
    }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public ConsoleHandler getConsoleHandler() {
        return consoleHandler;
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
