package me.kyllian.spigotconsole.commands;

import me.kyllian.spigotconsole.SpigotConsolePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotConsoleExecutor implements CommandExecutor {

    private final SpigotConsolePlugin plugin;

    public SpigotConsoleExecutor(SpigotConsolePlugin plugin) {
        this.plugin = plugin;

        plugin.getCommand("spigotconsole").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to execute this command!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("setup")) {
                if (!plugin.getRoleHandler().hasPermissionToRole(player)) {
                    player.sendMessage("You do not have permission to execute this command!");
                    return true;
                }
                plugin.getAppUserHandler().generateAppUser(player);
            }
        }
        return true;
    }
}
