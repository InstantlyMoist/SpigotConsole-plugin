package me.kyllian.spigotconsole.commands;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import me.kyllian.spigotconsole.SpigotConsolePlugin;
import me.kyllian.spigotconsole.handlers.QRCodeHandler;
import me.kyllian.spigotconsole.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.security.Key;

public class SpigotConsoleExecutor implements CommandExecutor {

    private SpigotConsolePlugin plugin;

    public SpigotConsoleExecutor(SpigotConsolePlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("throwerror")) {
                throw new IllegalStateException("You may not be here!");
            }
            if (args[0].equalsIgnoreCase("throwwarning")) {
                Bukkit.getLogger().warning("This is a warning >.<");
                return true;
            }
            if (args[0].equalsIgnoreCase("setup")) {
                Player player = (Player) commandSender;
                if (!player.isOp()) {
                    player.sendMessage(translateColor("&4You need to be OP to execute this command"));
                    return true;
                }
                PlayerData playerData = plugin.getPlayerDataHandler().getPlayerData(player);
                playerData.setInSetup(true);
                playerData.setHandItem(player.getInventory().getItemInMainHand());
                //TODO: CHECK IF KEY EXISTS ALREADY
                Key foundKey = plugin.getCipherHandler().getOrCreateKey(player);
                plugin.getMapHandler().sendMap(player, QRCodeHandler.generate(Base64.encode(foundKey.getEncoded())));
                return true;
            }
        }
        commandSender.sendMessage("&4Execute /spigotconsole setup");
        return true;
    }

    public String translateColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
