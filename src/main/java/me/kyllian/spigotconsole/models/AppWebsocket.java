package me.kyllian.spigotconsole.models;

import lombok.Getter;
import lombok.Setter;
import org.java_websocket.WebSocket;

public class AppWebsocket {

    @Getter
    private WebSocket webSocket;

    @Getter
    @Setter
    private AppUser appUser;

    @Getter
    private Role role;

    public AppWebsocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
