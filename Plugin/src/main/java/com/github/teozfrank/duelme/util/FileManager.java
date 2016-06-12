package com.github.teozfrank.duelme.util;

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

import com.github.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FileManager {
    private DuelMe plugin;

    public FileManager(DuelMe plugin) {
        this.plugin = plugin;
    }
    
    private FileConfiguration messages = null;
    private FileConfiguration duelArenas = null;
    private FileConfiguration signs;
    private File messagesFile = null;
    private File duelArenasFile = null;
    private File signsFile = null;


    public void reloadMessages() {
        if (messagesFile == null) {
            messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        InputStream defConfigStream = plugin.getResource("messages.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            messages.setDefaults(defConfig);
        }

    }

    /**
     * get the messages config object
     * @return the fileconfuration instance of the messages.yml file
     */
    public FileConfiguration getMessages() {
        if (messages == null) {
            this.reloadMessages();
        }
        return messages;
    }

    /**
     * save the messages.yml config file
     */
    public void saveMessages() {
        if (messages == null || messagesFile == null) {
            return;
        }
        try {
            this.getMessages().save(messagesFile);
        } catch (IOException e) {
            SendConsoleMessage.severe("Error saving messages config!");
        }
    }

    /**
     * save default messages.yml config
     */
    public void saveDefaultMessages() {
        if (messagesFile == null) {
            messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        }
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
    }

    /**
     * boolean to check if the duel is using seperated inventories
     * @return true if using them false if not
     */
    public boolean isUsingSeperateInventories(){
       boolean isUsingSeperateInventories = plugin.getConfig().getBoolean("duelme.duel.seperateinventories");
       return isUsingSeperateInventories;
    }

    /**
     * check and see if mysql stats are enabled
     * @return true if enabled, false if not
     */
    public boolean isMySqlEnabled() {
        boolean isMySqlEnabled = plugin.getConfig().getBoolean("duelme.mysql.enabled");
        return isMySqlEnabled;
    }

    /**
     * are drops when players die in a duel enabled
     * @return true if enabled, false if not
     */
    public boolean isDropItemsOnDeathEnabled() {
        boolean isDropsOnDeathEnabled = plugin.getConfig().getBoolean("duelme.duel.dropitemsondeath");
        return isDropsOnDeathEnabled;
    }


    /**
     * get the commands that are run on duel start
     * @return list of commands that are to be run
     */
    public List<String> getDuelStartCommands(){
        List<String> commands = plugin.getConfig().getStringList("duelme.commands.duelstart");
        return commands;
    }

    /**
     * get the minimum amount required to duel a player
     * @return the minimum amount
     */
    public int getMinDuelBetAmount() {
        return plugin.getConfig().getInt("duelme.duel.minbetamount");
    }

    /**
     * get the commands that are run on a duel ending
     * @return list of commands to be run
     */
    public List<String> getDuelWinnerCommands(){
        List<String> commands = plugin.getConfig().getStringList("duelme.commands.duelwinner");
        return commands;
    }

    /**
     * duelme debug mode
     * @return true if enabled, false if not
     */
    public boolean isDebugEnabled() {
        boolean isDebugEnabled = plugin.getConfig().getBoolean("duelme.debug.enabled");
        return isDebugEnabled;
    }

    /**
     * check to see if death messages are enabled
     * @return true if they are, false if not
     */
    public boolean isDeathMessagesEnabled(){
        boolean isDeathMessagesEnabled = plugin.getConfig().getBoolean("duelme.announce.deaths");
        return isDeathMessagesEnabled;
    }

    /**
     * reload the duel areas from arenas.yml
     */
    public void reloadDuelArenas() {
        if (duelArenasFile == null) {
            duelArenasFile = new File(plugin.getDataFolder(), "duelarenas.yml");
        }
        duelArenas = YamlConfiguration.loadConfiguration(duelArenasFile);

        InputStream defConfigStream = plugin.getResource("duelarenas.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            duelArenas.setDefaults(defConfig);
        }

    }

    public FileConfiguration getDuelArenas() {
        if (duelArenas == null) {
            this.reloadDuelArenas();
        }
        return duelArenas;
    }

    public void saveDefaultDuelArenas() {
        if (duelArenasFile == null) {
            duelArenasFile = new File(plugin.getDataFolder(), "duelarenas.yml");
        }
        if (!duelArenasFile.exists()) {
            plugin.saveResource("duelarenas.yml", false);
        }
    }

    /**
     * save the duel areas to disk
     */
    public void saveDuelArenas() {
        if (duelArenas == null) {
            reloadDuelArenas();
        }
        int savedArenas = 0;
        DuelManager dm = plugin.getDuelManager();

        if (dm.getDuelArenas().size() == 0) {
            SendConsoleMessage.info("There are no arenas to save.");
            return;
        }

        for (DuelArena a : dm.getDuelArenas()) {
            String path = "duelarenas." + a.getName() + ".";
            duelArenas.set(path + "pos1.world", a.getPos1().getWorld().getName());
            duelArenas.set(path + "pos1.x", a.getPos1().getBlockX());
            duelArenas.set(path + "pos1.y", a.getPos1().getBlockY());
            duelArenas.set(path + "pos1.z", a.getPos1().getBlockZ());
            duelArenas.set(path + "pos2.world", a.getPos2().getWorld().getName());
            duelArenas.set(path + "pos2.x", a.getPos2().getBlockX());
            duelArenas.set(path + "pos2.y", a.getPos2().getBlockY());
            duelArenas.set(path + "pos2.z", a.getPos2().getBlockZ());
            if(a.getSpawnpoint1() != null) {
                duelArenas.set(path + "spawnpoint1.world", a.getSpawnpoint1().getWorld().getName());
                duelArenas.set(path + "spawnpoint1.x", a.getSpawnpoint1().getX());
                duelArenas.set(path + "spawnpoint1.y", a.getSpawnpoint1().getY());
                duelArenas.set(path + "spawnpoint1.z", a.getSpawnpoint1().getZ());
                duelArenas.set(path + "spawnpoint1.yaw", Double.valueOf(a.getSpawnpoint1().getYaw()));
                duelArenas.set(path + "spawnpoint1.pitch", Double.valueOf(a.getSpawnpoint1().getPitch()));
            }
            if(a.getSpawnpoint2() != null) {
                duelArenas.set(path + "spawnpoint2.world", a.getSpawnpoint2().getWorld().getName());
                duelArenas.set(path + "spawnpoint2.x", a.getSpawnpoint2().getX());
                duelArenas.set(path + "spawnpoint2.y", a.getSpawnpoint2().getY());
                duelArenas.set(path + "spawnpoint2.z", a.getSpawnpoint2().getZ());
                duelArenas.set(path + "spawnpoint2.yaw", Double.valueOf(a.getSpawnpoint2().getYaw()));
                duelArenas.set(path + "spawnpoint2.pitch", Double.valueOf(a.getSpawnpoint2().getPitch()));
            }
            savedArenas++;
        }

        try {
            duelArenas.save(duelArenasFile);
            SendConsoleMessage.info("Successfully saved " + ChatColor.AQUA + savedArenas + ChatColor.GREEN + " Duel Arena(s).");
        } catch (Exception e) {
            SendConsoleMessage.severe("Error while saving Duel Arena(s)!");
        }

    }

    /**
     * load the duel arenas from disk
     */
    public void loadDuelArenas() {
        if (duelArenas == null) {
            reloadDuelArenas();
        }

        DuelManager dm = plugin.getDuelManager();

        ConfigurationSection sec = duelArenas.getConfigurationSection("duelarenas");

        if (sec == null)
            return;
        int loadedArenas = 0;

        Set<String> arenaSet = sec.getKeys(false);
        if (arenaSet != null) {
            for (String aArena : arenaSet) {
                String path = "duelarenas." + aArena + ".";
                String aName = ChatColor.translateAlternateColorCodes('&', aArena);
                String pos1w = duelArenas.getString(path + "pos1.world");
                int pos1x = duelArenas.getInt(path + "pos1.x");
                int pos1y = duelArenas.getInt(path + "pos1.y");
                int pos1z = duelArenas.getInt(path + "pos1.z");
                String pos2w = duelArenas.getString(path + "pos2.world");
                int pos2x = duelArenas.getInt(path + "pos2.x");
                int pos2y = duelArenas.getInt(path + "pos2.y");
                int pos2z = duelArenas.getInt(path + "pos2.z");

                if(duelArenas.isSet(path + "spawnpoint1") && duelArenas.isSet(path + "spawnpoint2")) {
                    String spawnpoint1w = duelArenas.getString(path + "spawnpoint1.world");
                    double spawnpoint1x = duelArenas.getDouble(path + "spawnpoint1.x");
                    double spawnpoint1y = duelArenas.getDouble(path + "spawnpoint1.y");
                    double spawnpoint1z = duelArenas.getDouble(path + "spawnpoint1.z");
                    float spawnpoint1yaw = (float) duelArenas.getDouble(path + "spawnpoint1.yaw");
                    float spawnpoint1pitch = (float) duelArenas.getDouble(path + "spawnpoint1.pitch");

                    String spawnpoint2w = duelArenas.getString(path + "spawnpoint2.world");
                    double spawnpoint2x = duelArenas.getDouble(path + "spawnpoint2.x");
                    double spawnpoint2y = duelArenas.getDouble(path + "spawnpoint2.y");
                    double spawnpoint2z = duelArenas.getDouble(path + "spawnpoint2.z");
                    float spawnpoint2yaw = (float) duelArenas.getDouble(path + "spawnpoint2.yaw");
                    float spawnpoint2pitch = (float) duelArenas.getDouble(path + "spawnpoint2.pitch");

                    Location spawnpoint1 = new Location(Bukkit.getWorld(spawnpoint1w), spawnpoint1x, spawnpoint1y, spawnpoint1z, spawnpoint1yaw, spawnpoint1pitch);
                    Location spawnpoint2 = new Location(Bukkit.getWorld(spawnpoint2w), spawnpoint2x, spawnpoint2y, spawnpoint2z, spawnpoint2yaw, spawnpoint2pitch);

                    Location pos1 = new Location(Bukkit.getWorld(pos1w), pos1x, pos1y, pos1z);
                    Location pos2 = new Location(Bukkit.getWorld(pos2w), pos2x, pos2y, pos2z);

                    DuelArena arena = new DuelArena(aName, pos1, pos2, spawnpoint1, spawnpoint2);

                    dm.addDuelArena(arena);
                    loadedArenas++;
                } else {
                    Location pos1 = new Location(Bukkit.getWorld(pos1w), pos1x, pos1y, pos1z);
                    Location pos2 = new Location(Bukkit.getWorld(pos2w), pos2x, pos2y, pos2z);

                    DuelArena arena = new DuelArena(aName, pos1, pos2);

                    dm.addDuelArena(arena);
                    loadedArenas++;
                }
            }
            SendConsoleMessage.info("Successfully loaded " + ChatColor.AQUA + loadedArenas + ChatColor.GREEN + " Duel Arena(s).");
        }
    }

    public void loadKits() {

        ConfigurationSection kitsSec = duelArenas.getConfigurationSection("kits");
    }

    /**
     * get the main config version
     * @return the version of the config
     */
    public double getConfigVersion(){
        double version = plugin.getConfig().getDouble("configversion");
        return version;
    }

    /**
     * is the plugin using right clicking a player
     * to send a duel request
     * @return true if enabled, false if not
     */
    public boolean isRightClickToDuelEnabled(){
        boolean usingRightClickToDuel = plugin.getConfig().getBoolean("duelme.duel.rightclicktoduel");
        return usingRightClickToDuel;
    }

    public boolean isDuelStartAnnouncementEnabled(){
        boolean isDuelStartAnnouncementEnabled = plugin.getConfig().getBoolean("duelme.announce.duelstart");
        return isDuelStartAnnouncementEnabled;
    }

    /**
     * get the min bet amount needed to send a bet duel request
     * @return the min bet amount
     */
    public double getMinBetAmount() {
        return plugin.getConfig().getDouble("duelme.duel.minbetamount");
    }

    /**
     * the time in seconds to countdown before duel starts
     * @return the time in seconds
     */
    public int getDuelCountdownTime() {
        return plugin.getConfig().getInt("duelme.duel.countdowntime");
    }

    /**
     * the time in seconds to how long a duel will last
     * @return the time in seconds
     */
    public int getDuelTime() {
        return plugin.getConfig().getInt("duelme.duel.dueltime");
    }

    /**
     * get the custom prefix of the plugin
     * @return the prefix of the plugin
     */
    public String getPrefix() {
        String prefix = getMessages().getString("messages.general.prefix");
        prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        return prefix;
    }

    public void reloadSigns() {
        if (signsFile == null) {
            signsFile = new File(plugin.getDataFolder(), "signs.yml");
        }
        signs = YamlConfiguration.loadConfiguration(signsFile);

        InputStream defConfigStream = plugin.getResource("signs.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            messages.setDefaults(defConfig);
        }

    }

    /**
     * get the signs config object
     * @return the fileconfuration instance of the signs.yml file
     */
    public FileConfiguration getSigns() {
        if (signs == null) {
            this.reloadSigns();
        }
        return signs;
    }

    /**
     * save the signs.yml config file
     */
    public void saveSigns() {
        if (signs == null || signsFile == null) {
            return;
        }
        try {
            this.getSigns().save(signsFile);
        } catch (IOException e) {
            SendConsoleMessage.severe("Error saving signs config!");
        }
    }

    /**
     * save default messages.yml config
     */
    public void saveDefaultSigns() {
        if (signsFile == null) {
            signsFile = new File(plugin.getDataFolder(), "signs.yml");
        }
        if (!signsFile.exists()) {
            plugin.saveResource("signs.yml", false);
        }
    }

    /**
     * save an arena status sign
     * @param arenaName the arena name
     * @param world the world name
     * @param x the x coordinate of the sign location
     * @param y the y coordinate of the sign location
     * @param z the z coordinate of the sign location
     */
    public void saveArenaSign(String arenaName, String world, int x, int y, int z) {
        String basePath = "signs." + arenaName + ".";
        getSigns().set(basePath + "world" , world);
        getSigns().set(basePath + "x" , x);
        getSigns().set(basePath + "y", y);
        getSigns().set(basePath + "z", z);
        saveSigns();
        reloadSigns();
    }

    /**
     * get and arena status sign location
     * @param arenaName the arena name
     */
    public Location getArenaStatusSignLocation(String arenaName) {
        String basePath = "signs." + arenaName;
        if(!getSigns().isSet(basePath)) {
            return null;
        }
        String worldName = getSigns().getString(basePath + ".world");
        int x = getSigns().getInt(basePath + ".x");
        int y = getSigns().getInt(basePath + ".y");
        int z = getSigns().getInt(basePath + ".z");

        Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
        return location;
    }

    /**
     * is the gui menu sent to players when a duel request is sent?
     * @return true if enabled false if not
     */
    public boolean isGUIMenuEnabled() {
        return plugin.getConfig().getBoolean("duelme.duel.guimenuenabled");
    }

    /**
     * get the max duel bet amount for a duel with bets
     * @return the max duel bet amount
     */
    public int getMaxBetAmount() {
        return plugin.getConfig().getInt("duelme.duel.maxbetamount");
    }

    /**
     * get the config version of the messages config file
     * @return the config version of the messages config file
     */
    public double getMessagesConfigVersion() {
        double version = this.getMessages().getDouble("configversion");
        return version;
    }

    /**
     * get the surround material for the spawnpoints when a duel is starting
     * @return the string material in bukkit material format
     */
    public String getDuelSurroundMaterial() {
        return plugin.getConfig().getString("duelme.duel.surroundmaterial");
    }
}
