package com.teozcommunity.teozfrank.duelme.main;

import com.teozcommunity.teozfrank.duelme.commands.DuelAdminCommand;
import com.teozcommunity.teozfrank.duelme.commands.DuelCommand;
import com.teozcommunity.teozfrank.duelme.events.*;
import com.teozcommunity.teozfrank.duelme.util.Locations;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Frank
 * Date: 19/07/13
 * Time: 21:53
 * To change this template use File | Settings | File Templates.
 */
public class DuelMe extends JavaPlugin {

    //String to hold our plugin prefix
    public String pluginPrefix;

    //boolean to keep track of if a duel is in progress
    public boolean inProgress;

    //String to hold the plugin version
    public String version;

    //String to to keep track of the dueling status
    public String duelStatus;

    //hashmap to keep track of the dueling requests
    public HashMap<String,String> duelRequests;

    //arraylist to hold the dueling players
    public ArrayList<Player> duelingPlayers;

    //arraylist to hold the spectating players
    public ArrayList<Player> spectatingPlayers;

    //arraylist to hold the frozen players (before a duel starts)
    public ArrayList<Player> frozenPlayers;

    //our util class
    public Util util;

    //our locations class
    public Locations locations;

    @Override
    public void onEnable(){
        if(!(new File(getDataFolder(), "config.yml")).exists())
        {
            saveDefaultConfig();
        }
        this.pluginPrefix = ChatColor.GOLD+"[DuelMe] ";
        this.version = this.getDescription().getVersion();
        this.inProgress = false;
        this.duelStatus = "WAITING";
        this.duelRequests = new HashMap<String, String>();
        this.duelingPlayers = new ArrayList<Player>();
        this.spectatingPlayers = new ArrayList<Player>();
        this.frozenPlayers = new ArrayList<Player>();
        this.util = new Util(this);
        this.locations = new Locations(this);
        this.registerCommands();
        this.registerEvents();

    }

    @Override
    public void onDisable(){

        Bukkit.getScheduler().cancelTasks(this);
    }

   /*
    * Register our plugin commands
    */
    public void registerCommands(){
       getCommand("duel").setExecutor(new DuelCommand(this));
       getCommand("dueladmin").setExecutor(new DuelAdminCommand(this));
    }

    public void registerEvents(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerMove(this), this);
        pm.registerEvents(new PlayerDeath(this),this);
        pm.registerEvents(new PlayerBreakBlock(this),this);
        pm.registerEvents(new PlayerQuit(this),this);
        pm.registerEvents(new PlayerTeleport(this),this);
        pm.registerEvents(new PlayerRespawn(this),this);
    }

}
