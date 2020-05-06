package me.kyllian.spigotconsole.handlers;

import me.kyllian.spigotconsole.SpigotConsolePlugin;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Plugin(name = "ConsoleHandler", category = "Core", elementType = "appender", printObject = true)
public class ConsoleHandler extends AbstractAppender {

    private SpigotConsolePlugin plugin;

    public ConsoleHandler(SpigotConsolePlugin plugin) {
        super("ConsoleHandler", null,
                PatternLayout.createDefaultLayout());
        this.plugin = plugin;
    }

    @Override
    public boolean isStarted() {
        return true;
    }

    @Override
    public void append(LogEvent e) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(e.getTimeMillis());
        String dateString = String.format("%d:%d:%d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        String finalString = String.format("[%s %s]: %s", dateString, e.getLevel().toString(), e.getMessage().getFormattedMessage());
        plugin.getConnectionHandler().broadcast("CONSOLE", finalString);
        // Do something with text
    }
}
