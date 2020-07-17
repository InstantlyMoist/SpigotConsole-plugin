package me.kyllian.spigotconsole.handlers;

import me.kyllian.spigotconsole.SpigotConsolePlugin;
import me.kyllian.spigotconsole.player.PlayerData;
import me.kyllian.spigotconsole.security.ResponseBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import javax.xml.ws.Response;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@WebSocket
public class ConnectionHandler {

    private SpigotConsolePlugin plugin;

    static List<Session> sessions = new LinkedList();
    static List<Session> verifiedSessions = new LinkedList();

    public ConnectionHandler(SpigotConsolePlugin plugin) {
        this.plugin = plugin;
    }

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        Bukkit.getLogger().info("Connected");
        sessions.add(user);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        Bukkit.getLogger().info("Disconnected");
        sessions.remove(user);
        verifiedSessions.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        new BukkitRunnable() {
            public void run() {
                JSONObject object = new JSONObject(message);
                String receivedType = object.getString("type");
                String receivedMessage = object.getString("message");
                switch (receivedType) {
                    case "INITIALHANDSHAKE":
                        if (plugin.getKeyFileHandler().keyExists(receivedMessage)) {
                            user.getRemote().sendStringByFuture(new ResponseBuilder().setType("INITIALHANDSHAKE").setMessage("OK").build());
                            sessions.remove(user);
                            verifiedSessions.add(user);

                            plugin.getConsoleHandler().getBuffer().forEach(bufferLine -> {
                                user.getRemote().sendStringByFuture(new ResponseBuilder().setType("CONSOLE").setMessage(bufferLine).build());
                            });

                            UUID foundUUID = plugin.getKeyFileHandler().getUUIDFromKey(receivedMessage);
                            if (foundUUID == null) return;
                            Player player = Bukkit.getPlayer(foundUUID);
                            if (player == null) return;
                            PlayerData playerData = plugin.getPlayerDataHandler().getPlayerData(player);
                            if (!playerData.isInSetup()) return;
                            playerData.setInSetup(false);
                            plugin.getMapHandler().resetMap(player.getInventory().getItemInMainHand());
                            player.getInventory().setItemInMainHand(playerData.getHandItem());

                            playerData.setHandItem(null);
                        } else {
                            user.getRemote().sendStringByFuture(new ResponseBuilder().setType("INITIALHANDSHAKE").setMessage("FAILED").build());
                            user.close();
                        }
                        break;
                    case "CONSOLE":
                        if (!verifiedSessions.contains(user)) return;
                        new BukkitRunnable() {
                            public void run() {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), receivedMessage);
                            }
                        }.runTask(plugin);
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void broadcast(String type, String message) {
        new BukkitRunnable() {
            public void run() {
                String response = new ResponseBuilder().setType(type).setMessage(message).build();
                for (Session session : verifiedSessions) {
                    try {
                        session.getRemote().sendStringByFuture(response);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
