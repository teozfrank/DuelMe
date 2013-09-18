package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Created with IntelliJ IDEA.
 * Original Author: teozfrank
 * Date: 07/07/13
 * Time: 21:31
 * -----------------------------
 * Removing this header is in breach of the license agreement,
 * please do not remove, move or edit it in any way.
 * -----------------------------
 */
public class SendConsoleMessage {

    private DuelMe plugin;
    private ConsoleCommandSender _cs;
    private final String prefix = ChatColor.GREEN + "[DuelMe] ";
    private final String info = "[Info] ";
    private final String severe = ChatColor.YELLOW + "[Severe] ";
    private final String warning = ChatColor.RED + "[Warning] ";

    public SendConsoleMessage(DuelMe plugin) {
        this.plugin = plugin;
        _cs = plugin.getServer().getConsoleSender();
    }

    public void info(String message) {
        _cs.sendMessage(prefix + info + message);
    }

    public void severe(String message) {
        _cs.sendMessage(prefix + severe + message);
    }

    public void warning(String message) {
        _cs.sendMessage(prefix + warning + message);
    }
}
