package me.kyllian.spigotconsole.handlers;

import me.kyllian.spigotconsole.SpigotConsolePlugin;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.Calendar;
import java.util.LinkedList;

@Plugin(name = "ConsoleHandler", category = "Core", elementType = "appender", printObject = true)
public class ConsoleHandler extends AbstractAppender {

    private SpigotConsolePlugin plugin;

    private LinkedList<String> buffer;
    private int bufferSize;

    public ConsoleHandler(SpigotConsolePlugin plugin) {
        super("ConsoleHandler", null,
                PatternLayout.createDefaultLayout());
        this.plugin = plugin;
        buffer = new LinkedList<>();
        bufferSize = plugin.getConfig().getInt("settings.buffer-size");
    }

    @Override
    public boolean isStarted() {
        return true;
    }

    @Override
    public void append(LogEvent e) {
        StringBuilder builder = new StringBuilder();
        Level logLevel = e.getLevel();
        switch (logLevel.toString()) {
            case "ERROR":
                builder.append("§4");
                break;
            case "INFO":
                builder.append("§a");
                break;
            case "WARN":
                builder.append("§e");
                break;
            default:
                builder.append("§f");
                break;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(e.getTimeMillis());
        String dateString = String.format("%d:%d:%d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        builder.append(String.format("[%s %s] ", dateString, e.getLevel().toString()));
        if (logLevel == Level.WARN) {
            if (e.getThrown() == null) builder.append(e.getMessage());
            else {
                builder.append(e.getThrown().getMessage());
                for (StackTraceElement stackTraceElement : e.getThrown().getStackTrace()) {
                    builder.append("\n\tat " + stackTraceElement.toString());
                }
            }
            if (e.getSource() != null) builder.append("Caused by").append("\t" + e.getSource().toString());
            builder.append("\n§4Errors may be incomplete, please refer to your original console for more information!");
        } else builder.append("§f").append(e.getMessage().getFormattedMessage());
        String finalMessage = builder.toString();
        adjustBuffer(finalMessage);
        plugin.getConnectionHandler().broadcast("CONSOLE", finalMessage);
    }

    public void adjustBuffer(String message) {
        if (buffer.size() > bufferSize) buffer.removeFirst();
        buffer.add(message);
    }

    public LinkedList<String> getBuffer() {
        return buffer;
    }
}
