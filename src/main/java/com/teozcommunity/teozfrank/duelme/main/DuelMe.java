package com.teozcommunity.teozfrank.duelme.main;

import com.teozcommunity.teozfrank.MetricsLite;
import com.teozcommunity.teozfrank.duelme.commands.DuelAdminExecutor;
import com.teozcommunity.teozfrank.duelme.commands.DuelExecutor;
import com.teozcommunity.teozfrank.duelme.events.*;
import com.teozcommunity.teozfrank.duelme.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;

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
     * player events class
     */
    private PlayerEvents playerEvents;

    /**
     * item manager class
     */
    private ItemManager itemManager;


    /**
     * string to hold the plugin version
     */
    private static String version;

    private int errorCount;

    public DuelMe(){
        this.errorCount = 0;
    }


    @Override
    public void onEnable() {
      SendConsoleMessage.info("Enabling.");
      this.version = this.getDescription().getVersion();
      this.fileManager = new FileManager(this);
      this.setupYMLs();
      this.checkForUpdates();
      this.submitStats();
      this.setupDependencies();
      this.duelManager = new DuelManager(this);
      this.playerEvents = new PlayerEvents(this);
      this.itemManager = new ItemManager(this);
      getCommand("duel").setExecutor(new DuelExecutor(this));
      getCommand("dueladmin").setExecutor(new DuelAdminExecutor(this));
      this.checkErrors();
    }

    private void checkErrors() {
        this.checkConfigVersions();
        if(this.errorCount != 0){
            SendConsoleMessage.warning(ChatColor.RED + "There were " + ChatColor.AQUA + errorCount +
                    ChatColor.RED + " startup error(s), plugin DISABLED!");
            this.getPluginLoader().disablePlugin(this);
            return;
        } else {
            SendConsoleMessage.info("Successfully Enabled!");
        }
    }


    @Override
    public void onDisable() {
      SendConsoleMessage.info("Disabling.");
      this.fileManager.saveDuelArenas();
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
            SendConsoleMessage.severe("Could not submit the stats! :(");
        }
    }

    public void setupYMLs() {
        if (!(new File(getDataFolder(), "config.yml")).exists()) {
            SendConsoleMessage.info("saving default config.yml.");
            saveDefaultConfig();
        }
        if (!(new File(getDataFolder(), "locations.yml")).exists()) {
            SendConsoleMessage.info("saving default locations.yml.");
            this.fileManager.saveDefaultLocations();
        }
        if (!(new File(getDataFolder(), "duelarenas.yml")).exists()) {
            SendConsoleMessage.info("saving default duelarenas.yml.");
            this.fileManager.saveDefaultDuelArenas();
        }
    }


    public void checkConfigVersions(){
        if(new File(getDataFolder(),"config.yml").exists()){
           if(fileManager.getConfigVersion() != 1.1){
               SendConsoleMessage.info("Your config.yml is out of date! please remove or back it up before using the plugin!");
               errorCount++;
           }
           if(fileManager.getLocationsVersion() != 1.1){
               SendConsoleMessage.info("Your locations.yml is out of date! please remove or back it up before using the plugin!");
               errorCount++;
           }
        }
    }

    /**
     * sets up the plugin main dependencies such as WorldEdit
     * disables the plugin if the required dependency is not present
     */
    private void setupDependencies() {
        if (this.getServer().getPluginManager().getPlugin("WorldEdit") != null) {
            SendConsoleMessage.info("WorldEdit found!");
        } else {
            SendConsoleMessage.warning("WorldEdit dependency not found, plugin disabled!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
    }

    public DuelManager getDuelManager(){
        return this.duelManager;
    }

    public FileManager getFileManager(){
        return this.fileManager;
    }

    public ItemManager getItemManager() { return itemManager; }

    public static String getVersion(){
        return version;
    }

}