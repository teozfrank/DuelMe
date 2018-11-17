package com.github.teozfrank.duelme.util;

import com.github.teozfrank.duelme.main.DuelMe;
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
        String killOtherMessage = fm.getMessages().getString("messages.deathcause.other");
        killOtherMessage = ChatColor.translateAlternateColorCodes('&', killOtherMessage);
        return killOtherMessage;
    }

    /**
     * get the kill message when a player is killed by another player in a duel
     * @return the kill message
     */
    public String getKillMessageByPlayer() {
        String killMessageByPlayer = fm.getMessages().getString("messages.deathcause.player");
        killMessageByPlayer = ChatColor.translateAlternateColorCodes('&', killMessageByPlayer);
        return killMessageByPlayer;
    }

    /**
     * get the announcement message when a duel is starting between two players
     * @return the annoucement message
     */
    public String getDuelStartMessage() {
        String duelStartMessage = fm.getMessages().getString("messages.starting.broadcast");
        duelStartMessage = ChatColor.translateAlternateColorCodes('&', duelStartMessage);
        return duelStartMessage;
    }

    /**
     * get the announcement message when a duel is started to the dueling players
     * @return the annoucement message
     */
    public String getDuelStartedMessage() {
        String duelStartedMessage = fm.getMessages().getString("messages.started.broadcast");
        duelStartedMessage = ChatColor.translateAlternateColorCodes('&', duelStartedMessage);
        return duelStartedMessage;
    }

    /**
     * get the message when a player leaves the duel
     * @return the message to show a player when they leave the duel
     */
    public String getDuelLeaveMessage() {
        String leaveDuel = fm.getMessages().getString("messages.duel.leaveduel");
        leaveDuel = ChatColor.translateAlternateColorCodes('&', leaveDuel);
        return leaveDuel;
    }

    /**
     * get the message when a player is not in a due
     * @return the message to show a player when they are not in a duel
     */
    public String getNotInDuelMessage() {
        String notInDuel = fm.getMessages().getString("messages.duel.notinduel");
        notInDuel = ChatColor.translateAlternateColorCodes('&', notInDuel);
        return notInDuel;
    }

    /**
     * get the message when their is no duel arenas
     * @return the message to show a player when their is no duel arenas
     */
    public String getNoDuelArenasMessage() {
        String noDuelArenas = fm.getMessages().getString("messages.error.noduelarenas");
        noDuelArenas = ChatColor.translateAlternateColorCodes('&', noDuelArenas);
        return noDuelArenas;
    }

    /**
     * get the message shown in the title message when the duel is starting
     * @return the message to show a player in the title when a duel is starting
     */
    public String getDuelStartingTitleMessage() {
        String startingTitleMessage = fm.getMessages().getString("messages.starting.title");
        startingTitleMessage = ChatColor.translateAlternateColorCodes('&', startingTitleMessage);
        return startingTitleMessage;
    }

    /**
     * get the duel starting subtitle message sent when a duel is starting
     * @return the duel starting subtitle message
     */
    public String getDuelStartingSubtitleMessage() {
        String startingSubtitleMessage = fm.getMessages().getString("messages.starting.subtitle");
        startingSubtitleMessage = ChatColor.translateAlternateColorCodes('&', startingSubtitleMessage);
        return startingSubtitleMessage;
    }

    /**
     * get the message countdown shown in the action bar when a duel has already started
     * @return the message to show a player in the action bar when a duel has already started
     */
    public String getDuelRemainingActionBarMessage() {
        String startedActionbar = fm.getMessages().getString("messages.started.actionbar");
        startedActionbar = ChatColor.translateAlternateColorCodes('&', startedActionbar);
        return startedActionbar;
    }

    /**
     * get the message when their is no duel arenas
     * @return the message to show a player when their is no duel arenas
     */
    public String getDuelForcefullyCancelledMessage() {
        String forcefullyCancelled = fm.getMessages().getString("messages.error.forcefullycancelled");
        forcefullyCancelled = ChatColor.translateAlternateColorCodes('&', forcefullyCancelled);
        return forcefullyCancelled;
    }

    /**
     * get the message shown to the winning player
     * @return the message to show a player that has won
     */
    public String getDuelWinMessage() {
        String duelWin = fm.getMessages().getString("messages.duel.duelwin");
        duelWin = ChatColor.translateAlternateColorCodes('&', duelWin);
        return duelWin;
    }

    /**
     * get the message shown to the player when they try to us a disallowed command
     * @return the message to show a player when they use a command that is not allowed
     */
    public String getCommandNotAllowedMessage() {
        String commandNotAllowed = fm.getMessages().getString("messages.error.commandnotallowed");
        commandNotAllowed = ChatColor.translateAlternateColorCodes('&', commandNotAllowed);
        return commandNotAllowed;
    }

    /**
     * get the message shown to the player when they send a duel request to another player
     * @return the message to show a player when send a duel request to another player
     */
    public String getDuelRequestSentMessage() {
        String duelRequestSent = fm.getMessages().getString("messages.request.sent");
        duelRequestSent = ChatColor.translateAlternateColorCodes('&', duelRequestSent);
        return duelRequestSent;
    }

    /**
     * get the message shown to the player when they receive a duel request
     * @return the message to show a player when they receive a duel request
     */
    public String getDuelRequestReceivedMessage() {
        String commandNotAllowed = fm.getMessages().getString("messages.request.received");
        commandNotAllowed = ChatColor.translateAlternateColorCodes('&', commandNotAllowed);
        return commandNotAllowed;
    }

    /**
     * get the message shown to the player they send a duel to a player that is not online
     * @return the message to show a player when they send a duel to a player that is not online
     */
    public String getTargetNotOnlineMessage() {
        String targetNotOnline = fm.getMessages().getString("messages.request.notonline");
        targetNotOnline = ChatColor.translateAlternateColorCodes('&', targetNotOnline);
        return targetNotOnline;
    }

    /**
     * get the message shown to the player if they try to duel themselves
     * @return the message to show a player when they try to duel themselves
     */
    public String getCannotDuelSelfMessage() {
        String cannotDuelSelf = fm.getMessages().getString("messages.error.cannottduelself");
        cannotDuelSelf = ChatColor.translateAlternateColorCodes('&', cannotDuelSelf);
        return cannotDuelSelf;
    }

    /**
     * get the message shown to the player if they try send another duel request to the same player that didnt respond
     * @return the message to show a player when they try send another duel request to the same player that didnt respond
     */
    public String getDuelRequestAlreadySentMessage() {
        String requestAlreadySent = fm.getMessages().getString("messages.request.alreadysent");
        requestAlreadySent = ChatColor.translateAlternateColorCodes('&', requestAlreadySent);
        return requestAlreadySent;
    }

    /**
     * get the message shown to the player if they try send a duel request to a player that is already in a duel
     * @return the message to show a player when they try send a duel request to a player that is already in a duel
     */
    public String getPlayerAlreadyInDuelMessage() {
        String playerAlreadyInDuel = fm.getMessages().getString("messages.request.alreadyinduel");
        playerAlreadyInDuel = ChatColor.translateAlternateColorCodes('&', playerAlreadyInDuel);
        return playerAlreadyInDuel;
    }

    /**
     * get the message sent to the player when a player is rewarded at the end of a duel
     * @return the message sent to the player when they are rewarded for winning
     */
    public String getDuelRewardMessage() {
        String duelRewardMessage = fm.getMessages().getString("messages.ended.reward");
        duelRewardMessage = ChatColor.translateAlternateColorCodes('&', duelRewardMessage);
        return duelRewardMessage;
    }

    /**
     * get the message sent to the player when they try to join the queue after
     * already joining the queue
     * @return the message sent to the player
     */
    public String getAlreadyInQueueMessage() {
        String alreadyInQueueMessage = fm.getMessages().getString("messages.queue.alreadyinqueue");
        alreadyInQueueMessage = ChatColor.translateAlternateColorCodes('&', alreadyInQueueMessage);
        return alreadyInQueueMessage;
    }

    /**
     * get the message sent to the player when they join the queue
     * @return the message sent to the player
     */
    public String getQueueJoinMessage() {
        String queueJoinMessage = fm.getMessages().getString("messages.queue.queuejoin");
        queueJoinMessage = ChatColor.translateAlternateColorCodes('&', queueJoinMessage);
        return queueJoinMessage;
    }

    /**
     * get the message sent to the player when there is not enough players in the queue to start a duel
     * @return the message sent to the player
     */
    public String getNotEnoughPlayersToStartMessage() {
        String notEnoughPlayersToStartMessage = fm.getMessages().getString("messages.queue.notenoughplayerstostart");
        notEnoughPlayersToStartMessage = ChatColor.translateAlternateColorCodes('&', notEnoughPlayersToStartMessage);
        return notEnoughPlayersToStartMessage;
    }
}
