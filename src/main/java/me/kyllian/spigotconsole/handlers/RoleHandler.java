package me.kyllian.spigotconsole.handlers;

import me.kyllian.spigotconsole.SpigotConsolePlugin;
import me.kyllian.spigotconsole.models.Role;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoleHandler {

    private SpigotConsolePlugin plugin;

    private List<Role> roles;

    public RoleHandler(SpigotConsolePlugin plugin) {
        this.plugin = plugin;

        load();
    }

    public void load() {
        roles = new ArrayList<>();

        plugin.getConfig().getConfigurationSection("roles").getKeys(false).forEach(role -> {
            String permission = plugin.getConfig().getString("roles." + role + ".permission");
            List<String> privileges = plugin.getConfig().getStringList("roles." + role + ".privileges");
            roles.add(new Role(role, permission, privileges));
        });
    }

    public Role getRoleByName(String name) {
        return roles.stream().filter(role -> role.getName().equals(name)).findFirst().orElse(null);
    }

    public Role getHighestRole(Player player) {
        return roles.stream().filter(role -> player.hasPermission(role.getPermission())).findFirst().orElse(null);
    }

    public boolean hasPermissionToRole(Player player) {
        return roles.stream().anyMatch(role -> player.hasPermission(role.getPermission()));
    }
}