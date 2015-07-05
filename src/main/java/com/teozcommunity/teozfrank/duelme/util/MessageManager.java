package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;


/**
 * Created by Frank on 05/07/2015.
 */
public class MessageManager {

    private DuelMe plugin;
    FileManager fm;

    public MessageManager(DuelMe plugin) {
        this.plugin = plugin;
        this.fm = plugin.getFileManager();
    }

    /**
     * get the kill message when a player is killed by something other than a player in a duel
     * @return the kill message
     */
    public String getKillOtherMessage() {
        String killOtherMessage = fm.getMessages().getString("messages.duel.deathcause.other");
        killOtherMessage = ChatColor.translateAlternateColorCodes('&', killOtherMessage);
        return killOtherMessage;
    }

    /**
     * get the kill message when a player is killed by another player in a duel
     * @return the kill message
     */
    public String getKillMessageByPlayer() {
        String killMessageByPlayer = fm.getMessages().getString("messages.duel.deathcause.player");
        killMessageByPlayer = ChatColor.translateAlternateColorCodes('&', killMessageByPlayer);
        return killMessageByPlayer;
    }

    /**
     * get the announcement message when a duel is starting between two players
     * @return the annoucement message
     */
    public String getDuelStartMessage() {
        String duelStartMessage = fm.getMessages().getString("messages.duel.starting.broadcast");
        duelStartMessage = ChatColor.translateAlternateColorCodes('&', duelStartMessage);
        return duelStartMessage;
    }

    /**
     * get the announcement message when a duel is started to the dueling players
     * @return the annoucement message
     */
    public String getDuelStartedMessage() {
        String duelStartedMessage = fm.getMessages().getString("messages.duel.started.broadcast");
        duelStartedMessage = ChatColor.translateAlternateColorCodes('&', duelStartedMessage);
        return duelStartedMessage;
    }
}
