package me.kyllian.spigotconsole.player;

import org.bukkit.inventory.ItemStack;

public class PlayerData {

    private boolean inSetup;
    private ItemStack handItem;

    public void setHandItem(ItemStack handItem) {
        this.handItem = handItem;
    }

    public ItemStack getHandItem() {
        return handItem;
    }

    public boolean isInSetup() {
        return inSetup;
    }

    public void setInSetup(boolean inSetup) {
        this.inSetup = inSetup;
    }
}
