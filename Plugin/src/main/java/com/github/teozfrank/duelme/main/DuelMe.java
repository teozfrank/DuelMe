package com.github.teozfrank.duelme.main;

/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2014 teozfrank
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import com.github.teozfrank.MetricsLite;
import com.github.teozfrank.duelme.api.TitleActionbar;
import com.github.teozfrank.duelme.commands.DuelAdminExecutor;
import com.github.teozfrank.duelme.commands.DuelExecutor;
import com.github.teozfrank.duelme.events.*;
import com.github.teozfrank.duelme.menus.AcceptMenu;
import com.github.teozfrank.duelme.util.*;
import com.github.teozfrank.duelme.mysql.MySql;
import com.github.teozfrank.duelme.threads.RequestTimeoutThread;
import com.github.teozfrank.duelme.threads.UpdateCheckerThread;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
     * message manager class
     */
    private MessageManager messageManager;

    /**
     * mysql class
     */
    private MySql mySql;

    private TitleActionbar titleActionbar;

    /**
     * string to hold the plugin version
     */
    private static String version;

    private int errorCount;

    private AcceptMenu acceptMenu;

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
        this.messageManager = new MessageManager(this);
        this.duelManager = new DuelManager(this);
        this.itemManager = new ItemManager(this);
        this.mySql = new MySql(this);
        getCommand("duel").setExecutor(new DuelExecutor(this));
        getCommand("dueladmin").setExecutor(new DuelAdminExecutor(this));
        this.getFileManager().loadDuelArenas();
        if(this.setupTitleActionBar()) {
            SendConsoleMessage.info("NMS Version setup complete");
        } else {
            SendConsoleMessage.severe("Error setting up NMS related needed classes! Please make sure you are using the correct version compatible with this plugin! Plugin DISABLED");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.checkErrors();
        if (errorCount != 0) {
            return;
        }
        this.registerEvents();
        this.startTasks();
    }

    private boolean setupTitleActionBar() {
        String packageName = this.getServer().getClass().getPackage().getName();
        // Get full package string of CraftServer.
        // org.bukkit.craftbukkit.version
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        // Get the last element of the package

        try {
            final Class<?> clazz = Class.forName("com.github.teozfrank.duelme.nms." + version + ".NMSHandler");
            // Check if we have a NMSHandler class at that location.
            if (TitleActionbar.class.isAssignableFrom(clazz)) { // Make sure it actually implements NMS
                this.titleActionbar = (TitleActionbar) clazz.getConstructor().newInstance(); // Set our handler
            }
        } catch (final Exception e) {
            if(isDebugEnabled()) {
                SendConsoleMessage.warning("Error setting up NMS Class. " + e.getMessage());
            }
            return false;
        }
        SendConsoleMessage.info("Loading support for " + version);
        return true;
    }

    private void startTasks() {
        this.getServer().getScheduler().runTaskTimer(this, new RequestTimeoutThread(this), 20L, 120L);
    }

    /**
     * register the event classes
     */
    private void registerEvents() {
        new PlayerEvents(this);
        new PlayerKick(this);
        new SignEdit(this);
        new PlayerDeath(this);
        new PlayerRespawn(this);
        this.acceptMenu = new AcceptMenu(this);
        new PlayerEvents(this);
        new SignEdit(this);
        new PlayerInteract(this);
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
            return;
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
            if (fileManager.getConfigVersion() != 1.7) {
                SendConsoleMessage.warning("Your config.yml is out of date! please remove or back it up before using the plugin!");
                errorCount++;
            }
        }

        if (new File(getDataFolder(), "messages.yml").exists()) {
            if (fileManager.getMessagesConfigVersion() != 1.2) {
                SendConsoleMessage.warning("Your messages.yml is out of date! please remove or back it up before using the plugin!");
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

    public static String getPrefix() {
        return prefix;
    }

    public AcceptMenu getAcceptMenu() {
        return acceptMenu;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public TitleActionbar getTitleActionbar() {
        return titleActionbar;
    }
}