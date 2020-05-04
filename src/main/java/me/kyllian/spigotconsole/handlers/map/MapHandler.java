package me.kyllian.spigotconsole.handlers.map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.image.BufferedImage;

public interface MapHandler {

    void loadData();
    void sendMap(Player player, BufferedImage image);
    void resetMap(ItemStack map);

}
