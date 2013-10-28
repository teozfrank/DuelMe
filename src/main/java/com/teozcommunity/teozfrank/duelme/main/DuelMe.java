package com.teozcommunity.teozfrank.duelme.main;

import com.teozcommunity.teozfrank.MetricsLite;
import com.teozcommunity.teozfrank.duelme.commands.DuelAdminCommand;
import com.teozcommunity.teozfrank.duelme.commands.DuelCommand;
import com.teozcommunity.teozfrank.duelme.events.*;
import com.teozcommunity.teozfrank.duelme.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * Original Author: teozfrank
 * Date: 19/07/13
 * Time: 21:53
 * -----------------------------
 * Removing this header is in breach of the license agreement,
 * please do not remove, move or edit it in any way.
 * -----------------------------
 */
public class DuelMe extends JavaPlugin {

    /**
     * String to hold our plugin prefix
     */
    public String pluginPrefix;

    /**
     * boolean to keep track of if a duel is in progress
     */
    public boolean inProgress;

    /**
     * String to hold the plugin version
     */
    public String version;

    /**
     * String to to keep track of the dueling status
     */
    public String duelStatus;

    /**
     * utilities class
     */
    public Util util;

    /**
     * locations class
     */
    public Locations locations;

    /**
     * update checker class
     */
    public UpdateChecker updateChecker;

    /**
     * coloured console sender class
     */
    public SendConsoleMessage sendConsoleMessage;

    /**
     * file manager class
     */
    public FileManager fileManager;

    /**
     * boolean to use separate inventories
     */
    public boolean seperateInventories;


    @Override
    public void onEnable() {
        this.seperateInventories = this.getConfig().getBoolean("duelme.duel.seperateinventories");
        this.sendConsoleMessage = new SendConsoleMessage(this);// called first so we can use the colored messages
        this.version = this.getDescription().getVersion();// called early so that any classes or methods that use this is available
        this.sendConsoleMessage.info("Enabling");
        this.fileManager = new FileManager(this);
        this.util = new Util(this);
        this.locations = new Locations(this);
        if (this.getConfig().getBoolean("duelme.checkforupdates")) {
            this.checkForUpdates();
            Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);
        }

        this.submitStats();
        this.setupYMLs(); //setup our config and other files

        this.pluginPrefix = ChatColor.GOLD + "[DuelMe] ";
        this.inProgress = false;
        this.duelStatus = "WAITING";
        this.duelRequests = new HashMap<String, String>();
        this.duelingPlayers = new ArrayList<Player>();
        this.spectatingPlayers = new ArrayList<Player>();
        this.frozenPlayers = new ArrayList<Player>();
        this.registerCommands();
        this.registerEvents();
        this.sendConsoleMessage.info("Enabled!");
    }


    @Override
    public void onDisable() {
        if (this.inProgress) {
            this.util.endDuel();
        }
        Bukkit.getScheduler().cancelTasks(this);
    }

    /*
     * Register our plugin commands
     */
    public void registerCommands() {
        getCommand("duel").setExecutor(new DuelCommand(this));
        getCommand("dueladmin").setExecutor(new DuelAdminCommand(this));
    }

    public void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerMove(this), this);
        pm.registerEvents(new PlayerDeath(this), this);
        pm.registerEvents(new PlayerBreakBlock(this), this);
        pm.registerEvents(new PlayerQuit(this), this);
        pm.registerEvents(new PlayerTeleport(this), this);
        pm.registerEvents(new PlayerRespawn(this), this);
        pm.registerEvents(new PlayerHitsPlayer(this), this);
    }

    public void checkForUpdates() {
        this.updateChecker = new UpdateChecker(this,60044);
    }

    public void submitStats() {
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            System.out.println("Failed to submit the stats :-(");
        }
    }

    public void setupYMLs() {
        if (!(new File(getDataFolder(), "config.yml")).exists()) {
            saveDefaultConfig();
        }
        if (!(new File(getDataFolder(), "locations.yml")).exists()) {
            this.fileManager.saveDefaultLocations();
        }
    }

    public boolean useSeperateInventories(){
        return this.seperateInventories;
    }
}