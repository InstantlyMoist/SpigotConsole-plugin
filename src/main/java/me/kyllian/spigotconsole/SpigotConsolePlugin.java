package me.kyllian.spigotconsole;

import lombok.Getter;
import me.kyllian.spigotconsole.commands.SpigotConsoleExecutor;
import me.kyllian.spigotconsole.files.KeyFile;
import me.kyllian.spigotconsole.files.LogFile;
import me.kyllian.spigotconsole.handlers.AppUserHandler;
import me.kyllian.spigotconsole.handlers.RoleHandler;
import me.kyllian.spigotconsole.security.EncryptionHandler;
import me.kyllian.spigotconsole.websockets.WebSocketHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotConsolePlugin extends JavaPlugin {

    @Getter
    private WebSocketHandler webSocketHandler;

    // Files
    @Getter
    private KeyFile keyFile;

    @Getter
    private LogFile logFile;

    // Handlers
    @Getter
    private AppUserHandler appUserHandler;
    @Getter
    private EncryptionHandler encryptionHandler;
    @Getter
    private RoleHandler roleHandler;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        initFiles();

        initHandlers();

        initWebsocket();

        initCommands();
    }

    @Override
    public void onDisable() {
//        webSocketHandler.stop();
    }

    public void initFiles() {
        keyFile = new KeyFile(this);

        logFile = new LogFile(this);
    }

    public void initHandlers() {
        roleHandler = new RoleHandler(this);

        encryptionHandler = new EncryptionHandler(this);

        appUserHandler = new AppUserHandler(this);
    }

    public void initWebsocket() {
        final int port = getConfig().getInt("port");

        webSocketHandler = new WebSocketHandler(this, port);
    }

    public void initCommands() {
        new SpigotConsoleExecutor(this);
    }
}
