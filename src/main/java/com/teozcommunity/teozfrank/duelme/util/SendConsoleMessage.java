package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
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

    private static final String prefix = ChatColor.GREEN + "[DuelMe] ";
    private static final String info = "[Info] ";
    private static final String severe = ChatColor.YELLOW + "[Severe] ";
    private static final String warning = ChatColor.RED + "[Warning] ";
    private static final String debug = ChatColor.AQUA + "[Debug] ";

    public SendConsoleMessage() {

    }

    public static void info(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + info + message);
    }

    public static void severe(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + severe + message);
    }

    public static void warning(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + warning + message);
    }

    public static void debug(String message){
        Bukkit.getConsoleSender().sendMessage(prefix + debug + message);
    }
}
