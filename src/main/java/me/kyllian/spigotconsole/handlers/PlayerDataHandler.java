package me.kyllian.spigotconsole.handlers;

import me.kyllian.spigotconsole.player.PlayerData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerDataHandler {

    private Map<Player, PlayerData> playerDataMap;

    public PlayerDataHandler() {
        playerDataMap = new HashMap<>();
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.computeIfAbsent(player, f -> new PlayerData());
    }
}
