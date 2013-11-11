package com.teozcommunity.teozfrank.duelme.main;

import com.teozcommunity.teozfrank.MetricsLite;
import com.teozcommunity.teozfrank.duelme.commands.DuelExecutor;
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
     * class to send coloured console messages
     */
    private SendConsoleMessage sendConsoleMessage;

    /**
     * duelmanager class
     */
    private DuelManager duelManager;

    /**
     * update checker class
     */
    private UpdateChecker updateChecker;

    /**
     * filemanager class
     */
    private FileManager fileManager;

    /**
     * string to hold the plugin version
     */
    private String version;


    @Override
    public void onEnable() {
      this.version = this.getDescription().getVersion();
      this.sendConsoleMessage = new SendConsoleMessage(this);
      this.sendConsoleMessage.info("Enabling.");
      this.fileManager = new FileManager(this);
      this.setupYMLs();
      this.checkForUpdates();
      this.submitStats();
      this.duelManager = new DuelManager(this);

      getCommand("duel").setExecutor(new DuelExecutor(this));
      this.sendConsoleMessage.info("Enabled!");
    }


    @Override
    public void onDisable() {

    }

    public void checkForUpdates() {
        if(this.getConfig().getBoolean("duelme.checkforupdates")){
            this.updateChecker = new UpdateChecker(this,60044);
        }
    }

    public void submitStats() {
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            this.sendConsoleMessage.severe("Could not submit the stats! :(");
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

    public DuelManager getDuelManager(){
        return this.duelManager;
    }

    public FileManager getFileManager(){
        return this.fileManager;
    }

    public SendConsoleMessage getConsoleMessageSender(){
        return this.sendConsoleMessage;
    }

    public String getVersion(){
        return this.version;
    }

}