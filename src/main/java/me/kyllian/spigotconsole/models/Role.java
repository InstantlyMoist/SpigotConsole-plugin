package me.kyllian.spigotconsole.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Role {

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String permission;
    private List<String> privileges;

    public Role(String name, String permission, List<String> privileges) {
        this.name = name;
        this.permission = permission;
        this.privileges = privileges;
    }

    public boolean hasPrivilege(String privilege) {
        return privileges.contains(privilege);
    }
}
