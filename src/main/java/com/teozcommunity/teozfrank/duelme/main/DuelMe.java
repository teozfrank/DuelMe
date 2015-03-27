package com.teozcommunity.teozfrank.duelme.main;

/**
 The MIT License (MIT)

 Copyright (c) 2014 teozfrank

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

import com.teozcommunity.teozfrank.MetricsLite;
import com.teozcommunity.teozfrank.duelme.commands.DuelAdminExecutor;
import com.teozcommunity.teozfrank.duelme.commands.DuelExecutor;
import com.teozcommunity.teozfrank.duelme.events.*;
import com.teozcommunity.teozfrank.duelme.mysql.MySql;
import com.teozcommunity.teozfrank.duelme.threads.UpdateCheckerThread;
import com.teozcommunity.teozfrank.duelme.util.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class DuelMe extends JavaPlugin {

    /**
     * duelmanager class
     */
    private DuelManager duelManager;

    /**
     * filemanager class
     */
    private FileManager fileManager;

    /**
     * item manager class
     */
    private ItemManager itemManager;

    /**
     * mysql class
     */
    private MySql mySql;

    private Economy economy = null;

    /**
     * string to hold the plugin version
     */
    private static String version;

    private int errorCount;

    public static String prefix;

    public DuelMe() {
        this.errorCount = 0;
    }


    @Override
    public void onEnable() {
        SendConsoleMessage.info("Enabling.");
        version = this.getDescription().getVersion();
        this.fileManager = new FileManager(this);
        prefix = getFileManager().getPrefix();
        this.setupYMLs();
        this.checkForUpdates();
        this.submitStats();
        this.setupDependencies();
        this.setupEconomy();
        this.duelManager = new DuelManager(this);
        this.registerEvents();
        new PlayerEvents(this);
        new SignEdit(this);
        this.itemManager = new ItemManager(this);
        this.mySql = new MySql(this);
        getCommand("duel").setExecutor(new DuelExecutor(this));
        getCommand("dueladmin").setExecutor(new DuelAdminExecutor(this));
        this.getFileManager().loadDuelArenas();
        this.checkErrors();
    }

    /**
     * register the event classes
     */
    private void registerEvents() {
        new EntityDamage(this);
        new PlayerEvents(this);
        new PlayerKick(this);
        new SignEdit(this);
    }

    @Override
    public void onDisable() {
        SendConsoleMessage.info("Disabling.");
        this.endAllRunningDuels();
        this.getFileManager().saveDuelArenas();
    }

    /**
     * end all the currently running duels
     */
    private void endAllRunningDuels() {
        DuelManager dm = this.getDuelManager();
        if (dm.getDuelArenas().size() == 0) {//if there are no duel arenas
            return;
        }
        for (DuelArena duelArena : dm.getDuelArenas()) {
            dm.endDuel(duelArena);
        }

    }

    /**
     * Check for startup errors for outdated plugin configs
     */
    private void checkErrors() {
        this.checkConfigVersions();
        if (this.errorCount != 0) {
            SendConsoleMessage.warning(ChatColor.RED + "There were " + ChatColor.AQUA + errorCount +
                    ChatColor.RED + " startup error(s), plugin DISABLED!");
            this.getPluginLoader().disablePlugin(this);
        } else {
            SendConsoleMessage.info("Successfully Enabled!");
        }
    }

    /**
     * check for updates to the plugin
     */
    public void checkForUpdates() {
        if (this.getConfig().getBoolean("duelme.checkforupdates")) {
            getServer().getScheduler().runTask(this, new UpdateCheckerThread(this));
        }
    }

    /**
     * attempt to submit the plugin stats
     * to mcstats.org
     */
    public void submitStats() {
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            SendConsoleMessage.severe("Could not submit the stats! :(");
        }
    }

    /**
     * setup the yml files used in the plugin
     */
    public void setupYMLs() {
        if (!(new File(getDataFolder(), "config.yml")).exists()) {
            SendConsoleMessage.info("Saving default config.yml.");
            saveDefaultConfig();
        }
        if (!(new File(getDataFolder(), "duelarenas.yml")).exists()) {
            SendConsoleMessage.info("Saving default duelarenas.yml.");
            this.getFileManager().saveDefaultDuelArenas();
        }

        if (!(new File(getDataFolder(), "messages.yml")).exists()) {
            SendConsoleMessage.info("Saving default messages.yml.");
            this.getFileManager().saveDefaultMessages();
        }

        if (!(new File(getDataFolder(), "signs.yml")).exists()) {
            SendConsoleMessage.info("Saving default signs.yml.");
            this.getFileManager().saveDefaultSigns();
        }
    }

    /**
     * check the config file versions and see
     * do they match the latest version
     */
    public void checkConfigVersions() {
        if (new File(getDataFolder(), "config.yml").exists()) {
            if (fileManager.getConfigVersion() != 1.4) {
                SendConsoleMessage.warning("Your config.yml is out of date! please remove or back it up before using the plugin!");
                errorCount++;
            }
        }
    }

    /**
     * setup economy
     *
     * @return economy object
     */
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
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
        }
    }

    /**
     * get the duel manager object
     *
     * @return duel manager object
     */
    public DuelManager getDuelManager() {
        return this.duelManager;
    }

    /**
     * get the file manager object
     *
     * @return file manager object
     */
    public FileManager getFileManager() {
        return this.fileManager;
    }

    /**
     * get the item manager object
     *
     * @return item manager object
     */
    public ItemManager getItemManager() {
        return itemManager;
    }

    /**
     * get the plugin version
     *
     * @return the plugin version
     */
    public static String getVersion() {
        return version;
    }

    /**
     * get MySql object
     *
     * @return the MySql object
     */
    public MySql getMySql() {
        return this.mySql;
    }

    /**
     * is debug mode enabled
     *
     * @return true if enabled, false if not
     */
    public boolean isDebugEnabled() {
        return getFileManager().isDebugEnabled();
    }

    public boolean isUsingSeperatedInventories() {
        return this.getFileManager().isUsingSeperateInventories();
    }

    public Economy getEconomy() {
        return economy;
    }

    public static String getPrefix() {
        return prefix;
    }

}