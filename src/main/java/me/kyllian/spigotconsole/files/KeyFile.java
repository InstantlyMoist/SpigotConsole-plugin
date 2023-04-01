package me.kyllian.spigotconsole.files;

import me.kyllian.spigotconsole.SpigotConsolePlugin;

public class KeyFile extends FlatFile {

    /*
     * File structure:
     * uuid:
     *  key: key
     *  role : role
     */

    public KeyFile(SpigotConsolePlugin plugin) {
        super(plugin, "keys.yml");
    }
}
