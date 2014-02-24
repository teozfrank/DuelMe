package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Original Author: teozfrank
 * Date: 16/08/13
 * Time: 21:40
 * -----------------------------
 * Removing this header is in breach of the license agreement,
 * please do not remove, move or edit it in any way.
 * -----------------------------
 */
public class FileManager {
    private DuelMe plugin;

    public FileManager(DuelMe plugin) {
        this.plugin = plugin;
    }


    private FileConfiguration locations = null;
    private FileConfiguration messages = null;
    private FileConfiguration duelArenas = null;
    private File locationsFile = null;
    private File messagesFile = null;
    private File duelArenasFile = null;


    public void reloadLocations() {
        if (locationsFile == null) {
            locationsFile = new File(plugin.getDataFolder(), "locations.yml");
        }
        locations = YamlConfiguration.loadConfiguration(locationsFile);

        InputStream defConfigStream = plugin.getResource("locations.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            locations.setDefaults(defConfig);
        }

    }

    public FileConfiguration getLocations() {
        if (locations == null) {
            this.reloadLocations();
        }
        return locations;
    }

    public void saveLocations() {
        if (locations == null || locationsFile == null) {
            return;
        }
        try {
            this.getLocations().save(locationsFile);
        } catch (IOException e) {
           SendConsoleMessage.severe("Error saving locations config!");
        }
    }

    public void saveDefaultLocations() {
        if (locationsFile == null) {
            locationsFile = new File(plugin.getDataFolder(), "locations.yml");
        }
        if (!locationsFile.exists()) {
            plugin.saveResource("locations.yml", false);
        }
    }

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
            this.getLocations().save(messagesFile);
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
     * get the commands that are run on duel start
     * @return list of commands that are to be run
     */
    public List<String> getDuelStartCommands(){
        List<String> commands = plugin.getConfig().getStringList("duelme.commands.duelstart");
        return commands;
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

                Location pos1 = new Location(Bukkit.getWorld(pos1w), pos1x, pos1y, pos1z);
                Location pos2 = new Location(Bukkit.getWorld(pos2w), pos2x, pos2y, pos2z);

                DuelArena arena = new DuelArena(aName, pos1, pos2);


                dm.addDuelArena(arena);
                loadedArenas++;
                SendConsoleMessage.info("Successfully loaded " + ChatColor.AQUA + loadedArenas + ChatColor.GREEN + " Duel Arena(s).");
            }
        }
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
     * get the locations.yml config version
     * @return the version of the config
     */
    public double getLocationsVersion(){
        double version = this.getLocations().getDouble("configversion");
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
     * gets the lobby spawn location from locations configuration file
     * @return lobby spawn location
     */
    public Location getLobbySpawnLocation() {
        FileManager fm = plugin.getFileManager();
        String WorldIn = fm.getLocations().getString("locations.lobbyspawn.world");
        double targetxIn = fm.getLocations().getDouble("locations.lobbyspawn.x");
        double targetyIn = fm.getLocations().getDouble("locations.lobbyspawn.y");
        double targetzIn = fm.getLocations().getDouble("locations.lobbyspawn.z");

        World targetWorld = Bukkit.getWorld(WorldIn);

        Location lobbySpawnLoc = new Location(targetWorld, targetxIn, targetyIn + 0.5, targetzIn);

        return lobbySpawnLoc;
    }


    /**
     * sets the lobby spawn location to disk and reloads configuration file
     * @param p the player thats setting the lobby spawn location
     */
    public void setLobbySpawnLocation(Player p) {
        FileManager fm = plugin.getFileManager();
        Location loc = p.getLocation();
        String world = p.getWorld().getName();

        double senderxIn = loc.getX();
        double senderyIn = loc.getY();
        double senderzIn = loc.getZ();

        fm.getLocations().set("locations.lobbyspawn.world", world);
        fm.getLocations().set("locations.lobbyspawn.x", senderxIn);
        fm.getLocations().set("locations.lobbyspawn.y", senderyIn);
        fm.getLocations().set("locations.lobbyspawn.z", senderzIn);
        fm.saveLocations();
        fm.reloadLocations();
        Util.sendMsg(p, ChatColor.GREEN + "Lobby Spawn Location set to: "
                + ChatColor.GOLD +"(X: " + loc.getBlockX() + ") (Y: "+ loc.getBlockY() + ") (Z: " + loc.getBlockZ() + ")");
    }

}
