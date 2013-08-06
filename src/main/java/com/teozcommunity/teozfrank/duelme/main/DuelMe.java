package com.teozcommunity.teozfrank.duelme.main;

import com.teozcommunity.teozfrank.duelme.commands.DuelCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

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

    //hashmap to keep track of the dueling requests
    public HashMap<String,String> requests;

    //arraylist to hold the dueling players
    public ArrayList<String> duelingPlayers;

    //arraylist to hold the spectating players
    public ArrayList<String> spectatingPlayers;

    //arraylist to hold the frozen players (before a duel starts)
    public ArrayList<String> frozenPlayers;

    @Override
    public void onEnable(){
        this.pluginPrefix = ChatColor.GOLD+"[DuelMe] ";
        this.inProgress = false;
        this.requests = new HashMap<String, String>();
        this.duelingPlayers = new ArrayList<String>();
        this.spectatingPlayers = new ArrayList<String>();
        this.frozenPlayers = new ArrayList<String>();
        this.registerCommands();

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
    }

}
