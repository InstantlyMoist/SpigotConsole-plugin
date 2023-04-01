package me.kyllian.spigotconsole.websockets;

import me.kyllian.spigotconsole.SpigotConsolePlugin;
import me.kyllian.spigotconsole.models.AppUser;
import me.kyllian.spigotconsole.models.AppWebsocket;
import org.bukkit.Bukkit;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.nio.ByteBuffer;

public class WebSocketHandler extends WebSocketServer {

    private final SpigotConsolePlugin plugin;

    private List<AppWebsocket> connections;

    public WebSocketHandler(SpigotConsolePlugin plugin, int port) {
        super(new InetSocketAddress(port));
        start();

        Bukkit.getLogger().info("Running at  " + getAddress());

        this.plugin = plugin;

        connections = new ArrayList<>();
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        AppWebsocket appWebsocket = new AppWebsocket(webSocket);
        connections.add(appWebsocket);
    }

    @Override
    public void onClose(WebSocket connection, int code, String reason, boolean remote) {
        AppWebsocket appWebsocket = getAppWebsocket(connection);
        if (appWebsocket == null) {
            throw new RuntimeException("Closed a connection we didn't know we had!");
        }
        connections.remove(appWebsocket);
    }

    @Override
    public void onMessage(WebSocket connection, String message) {
        AppWebsocket appWebsocket = getAppWebsocket(connection);
        if (appWebsocket == null) {
            throw new RuntimeException("Got a message from a connection we didn't know we had!");
        }

        if (appWebsocket.getAppUser() == null) {
            // User has not handshaked yet.
            UUID uuid;
            try {
                uuid = UUID.fromString(message);
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().info("User has not handshaked yet, but sent an invalid UUID!");
                // Close the connection.
                connection.close();
                return;
            }
            AppUser foundUser = plugin.getAppUserHandler().getAppUser(uuid);
            if (foundUser == null) {
                Bukkit.getLogger().info("User does not exist!");
                // User does not exist.
                connection.close();
                return;
            }
            appWebsocket.setAppUser(foundUser);
            Bukkit.getLogger().info("User has handshaked!, key: " + foundUser.getKey());
            return;
        }

        Bukkit.getLogger().info("Got message: " + message);
        plugin.getLogFile().log("Got message: " + message);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {

    }

    public void stop() {
        try {
            super.stop();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public AppWebsocket getAppWebsocket(WebSocket webSocket) {
        return connections.stream().filter(appWebsocket -> appWebsocket.getWebSocket().equals(webSocket)).findFirst().orElse(null);
    }

    public void broadcast(String message) {
        connections.forEach(appWebsocket -> {
            String encryptedMessage = plugin.getEncryptionHandler().encrypt(message, appWebsocket.getAppUser());
            appWebsocket.getWebSocket().send(encryptedMessage);
        });
    }
}
