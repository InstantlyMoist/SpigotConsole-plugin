package me.kyllian.spigotconsole.commands;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.org.omg.CORBA.ExcDescriptionSeqHelper;
import me.kyllian.spigotconsole.SpigotConsolePlugin;
import me.kyllian.spigotconsole.handlers.QRCodeHandler;
import me.kyllian.spigotconsole.player.PlayerData;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.security.Key;

public class SpigotConsoleExecutor implements CommandExecutor {

    private SpigotConsolePlugin plugin;

    public SpigotConsoleExecutor(SpigotConsolePlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        if (args[0].equalsIgnoreCase("setup")) {
            Player player = (Player) commandSender;
            PlayerData playerData = plugin.getPlayerDataHandler().getPlayerData(player);
            playerData.setInSetup(true);
            //TODO: CHECK IF KEY EXISTS ALREADY
            Key foundKey = plugin.getCipherHandler().getOrCreateKey(player);
            plugin.getMapHandler().sendMap(player, QRCodeHandler.generate(Base64.encode(foundKey.getEncoded())));
        }
        return true;
    }
}
