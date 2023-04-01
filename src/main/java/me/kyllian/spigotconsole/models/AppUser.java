package me.kyllian.spigotconsole.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.security.Key;
import java.util.UUID;

public class AppUser implements Serializable {

    @Getter
    private UUID uuid;
    @Getter
    private Key key;

    @Getter
    @Setter
    private Role role;

    @Getter
    @Setter
    private boolean confirmed;

    public AppUser(UUID uuid, Key key, Role role) {
        this.uuid = uuid;
        this.key = key;
        this.role = role;
    }
}
