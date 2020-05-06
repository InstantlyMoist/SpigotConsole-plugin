package me.kyllian.spigotconsole.handlers;

import jdk.nashorn.internal.runtime.ECMAException;
import me.kyllian.spigotconsole.SpigotConsolePlugin;
import me.kyllian.spigotconsole.security.ResponseBuilder;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;
import sun.reflect.annotation.ExceptionProxy;

import javax.xml.ws.Response;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class ConnectionHandler {

    private SpigotConsolePlugin plugin;

    static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();

    public ConnectionHandler(SpigotConsolePlugin plugin) {
        this.plugin = plugin;
    }

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "User" + (userUsernameMap.size() + 1);
        Bukkit.getLogger().info("Connected");
        userUsernameMap.put(user, username);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = userUsernameMap.get(user);
        Bukkit.getLogger().info("Disconnected");
        userUsernameMap.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        JSONObject object = new JSONObject(message);
        String receivedType = object.getString("type");
        String receivedMessage = object.getString("message");
        try {
            switch (receivedType) {
                case "INITIALHANDSHAKE":
                    if (plugin.getKeyFileHandler().keyExists(receivedMessage)) {
                        user.getRemote().sendString(new ResponseBuilder().setType("INITIALHANDSHAKE").setMessage("OK").build());
                    } else {
                        user.getRemote().sendString(new ResponseBuilder().setType("INITIALHANDSHAKE").setMessage("FAILED").build());
                        user.close();
                    }
                    break;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Bukkit.getLogger().info(message);
    }

    public void broadcast(String type, String message) {
        String response = new ResponseBuilder().setType(type).setMessage(message).build();
        for (Session session : userUsernameMap.keySet()) {
            try {
                session.getRemote().sendString(response);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
