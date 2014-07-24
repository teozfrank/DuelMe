package com.teozcommunity.teozfrank.duelme.commands.admin;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelArena;
import com.teozcommunity.teozfrank.duelme.util.FileManager;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by frank on 10/02/14.
 */
public class ReloadCmd extends DuelAdminCmd {
    public ReloadCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(DuelArena duelArena, CommandSender sender, String subCmd, String[] args) {

        FileManager fm = plugin.getFileManager();
        Util.sendMsg(sender, ChatColor.YELLOW + "Reloading.");
        fm.reloadDuelArenas();
        Util.sendMsg(sender, ChatColor.YELLOW + "Reloaded duelarenas.yml!");
        fm.reloadLocations();
        Util.sendMsg(sender, ChatColor.YELLOW + "Reloaded locations.yml!");
        plugin.reloadConfig();
        Util.sendMsg(sender, ChatColor.YELLOW + "Reloaded config.yml!");
        Util.sendMsg(sender, ChatColor.YELLOW + "Saving Duel arenas!");
        fm.saveDuelArenas();
        Util.sendMsg(sender, ChatColor.YELLOW + "Loading Duel arenas!");
        fm.loadDuelArenas();
        Util.sendMsg(sender, ChatColor.GREEN + "Complete!");
    }
}
