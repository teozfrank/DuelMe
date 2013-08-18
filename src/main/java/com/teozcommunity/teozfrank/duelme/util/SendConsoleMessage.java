package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;


/**
 * Created with IntelliJ IDEA.
 * User: Frank
 * Date: 07/07/13
 * Time: 21:31
 * To change this template use File | Settings | File Templates.
 */
public class SendConsoleMessage {


    private DuelMe plugin;
    private ConsoleCommandSender _cs;
    private String prefix = ChatColor.GREEN+"[DuelMe] ";
    private String info = "[Info] ";
    private String severe = ChatColor.YELLOW+"[Severe] ";
    private String warning = ChatColor.RED+"[Warning] ";


    public SendConsoleMessage(DuelMe plugin){
        this.plugin = plugin;
        _cs = plugin.getServer().getConsoleSender();
    }

    public void info(String message){
       _cs.sendMessage(prefix + info + message);
    }

    public void severe(String message){
        _cs.sendMessage(prefix + severe + message);
    }

    public void warning(String message){
        _cs.sendMessage(prefix + warning + message);
    }

}
