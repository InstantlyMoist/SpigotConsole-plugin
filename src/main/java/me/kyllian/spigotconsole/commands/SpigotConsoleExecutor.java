package me.kyllian.spigotconsole.commands;

import me.kyllian.spigotconsole.security.CipherHandler;
import me.kyllian.spigotconsole.security.KeyFactory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sun.reflect.annotation.ExceptionProxy;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SpigotConsoleExecutor implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        if (args[0].equalsIgnoreCase("setup")) {
            Player player = (Player) commandSender;
            String baseString = "Hallo wereld";
            String encrypted = CipherHandler.encrypt(baseString, player.getUniqueId());
            String decrypted = CipherHandler.decrypt(encrypted, player.getUniqueId());

            Bukkit.broadcastMessage("Base:" + baseString);
            Bukkit.broadcastMessage("Encrypted:" + encrypted);
            Bukkit.broadcastMessage("Decrypted:" + decrypted);
            //TODO: Make sure playerdata get set correctly
        }
        return true;
    }
}
