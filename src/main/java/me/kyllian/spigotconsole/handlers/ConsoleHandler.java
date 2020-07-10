package me.kyllian.spigotconsole.handlers;

import me.kyllian.spigotconsole.SpigotConsolePlugin;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.Calendar;

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
        StringBuilder builder = new StringBuilder();
        Level logLevel = e.getLevel();
        switch(logLevel.toString()) {
            case "ERROR": builder.append("§4"); break;
            case "INFO": builder.append("§a"); break;
            case "WARN": builder.append("§e"); break;
            default: builder.append("§f"); break;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(e.getTimeMillis());
        String dateString = String.format("%d:%d:%d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        builder.append(String.format("[%s %s]:", dateString, e.getLevel().toString()));
        if (logLevel != Level.WARN) builder.append("§f");
        builder.append(e.getMessage().getFormattedMessage());
        plugin.getConnectionHandler().broadcast("CONSOLE", builder.toString());
        // Do something with text
    }
}
