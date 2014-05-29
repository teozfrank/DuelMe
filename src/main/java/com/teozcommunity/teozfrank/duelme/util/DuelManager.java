package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.threads.StartDuelThread;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: teoz
 * Date: 26/10/13
 * Time: 18:18
 * To change this template use File | Settings | File Templates.
 */
public class DuelManager {

    private DuelMe plugin;

    /**
     * map to keep track of the dueling requests
     * the key is the duel sender
     * the value is the player who has been sent a request
     */
    public Map<String, String> duelRequests;

    /**
     * list to hold the current spectating players
     */
    public List<String> spectatingPlayers;

    /**
     * list to hold the frozen players (before a duel starts)
     */
    public List<String> frozenPlayers;

    /**
     * list to hold the dead players
     */
    public List<String> deadPlayers;

    /**
     * list to hold arena objects
     */
    public List<DuelArena> duelArenas;

    /**
     * hashmap to store players inventories
     */
    private static HashMap<String, ItemStack[]> inventories;

    /**
     * hashmap to store players armour
     */
    private static HashMap<String, ItemStack[]> armour;

    public DuelManager(DuelMe plugin){
        this.plugin = plugin;
        this.duelRequests = new HashMap<String, String>();
        this.spectatingPlayers = new ArrayList<String>();
        this.frozenPlayers = new ArrayList<String>();
        this.duelArenas = new ArrayList<DuelArena>();
        this.deadPlayers = new ArrayList<String>();
        this.inventories = new HashMap<String, ItemStack[]>();
        this.armour = new HashMap<String, ItemStack[]>();
    }

    /**
     * gets a list of the arena objects
     * @return list of arenas
     */
    public List<DuelArena> getDuelArenas(){
        return duelArenas;
    }

    /**
     * add a duel arena
     * @param da the duel arena
     */
    public void addDuelArena(DuelArena da){
        this.duelArenas.add(da);
    }

    /**
     * get a duel arena by name
     * @param duelArenaName the duel arena name
     * @return the duel arena , null if it does not exist
     */
    public DuelArena getDuelArenaByName(String duelArenaName){
        for(DuelArena da: duelArenas){
            if(da.getName().equalsIgnoreCase(duelArenaName)){
                return da;
            }
        }
        return null;
    }


    /**
     * if a player is in a duel
     * @param playerName the players name
     * @return true if is in a duel, false if not
     */
    public boolean isInDuel(String playerName){
        for(DuelArena a: this.getDuelArenas()){
            if(a.getPlayers().contains(playerName)){
                return true;
            }
        }
        return false;
    }

    /**
     * get the arena name that a player is in
     * @param playerName the players name
     * @return the arena name that the plater is in,
     * returns null if the player is not in an arena
     */
    public String getPlayersArenaName(String playerName){
        for(DuelArena a: this.getDuelArenas()){
            if(a.getPlayers().contains(playerName)){
                return a.getName();
            }
        }
        return null;
    }

    /**
     * gets the arena of two players
     * @param player1 the first player
     * @param player2 the second player
     * @return the arena that the players are in
     * , null if both players are not in the same arena.
     */
    public DuelArena getPlayersArena(String player1,String player2) {

        for(DuelArena a: this.getDuelArenas()){
            List<String> players = a.getPlayers();
            if(players.contains(player1) && players.contains(player2)){
                return a;
            }
        }
        return null;
    }

    public boolean isFrozen(String playerIn){
        if(this.getFrozenPlayers().contains(playerIn)){
            return true;
        }
        return false;
    }

    /**
     * get a list of the frozen players
     * @return list of frozen players
     */
    public List<String> getFrozenPlayers(){
        return this.frozenPlayers;
    }

    /**
     * add a frozen player to stop them from moving
     * @param playerName the players name
     */
    public void addFrozenPlayer(String playerName){
        this.frozenPlayers.add(playerName);
    }

    /**
     * add a frozen players to stop them from moving
     * @param senderName the duel sender
     * @param targetName the duel target
     */
    public void addFrozenPlayer(String senderName, String targetName){
        this.frozenPlayers.add(senderName);
        this.frozenPlayers.add(targetName);
    }

    /**
     * remove a frozen player allowing them to move
     * @param playerName the players name
     */
    public void removeFrozenPlayer(String playerName){
        this.frozenPlayers.remove(playerName);
    }

    /**
     * gets the arena of a player
     * @param player the players name
     * @return the arena of the player, null if the player
     * is not in a arena
     */
    public DuelArena getPlayersArena(String player) {

        for(DuelArena a: this.getDuelArenas()){
            List<String> players = a.getPlayers();
            if(players.contains(player)){
                return a;
            }
        }
        return null;
    }

    /**
     * handle duel requests
     * @param duelSender the sender of the request
     * @param duelTargetIn the string player of the target player
     */
    public void sendRequest(Player duelSender,String duelTargetIn){

        String duelSenderName = duelSender.getName();

        if(this.duelRequests.containsKey(duelSenderName) && this.duelRequests.containsValue(duelTargetIn)){
            Util.sendMsg(duelSender,ChatColor.YELLOW+"You have already sent a request to " +
                    ChatColor.AQUA + duelTargetIn + ".");
            return;
        }

        Player duelTarget = Bukkit.getPlayer(duelTargetIn);

        if(duelTarget != null){

            String duelTargetName = duelTarget.getName();
            if(duelSenderName == duelTargetName){
                Util.sendMsg(duelSender,ChatColor.RED+"You cannot duel yourself!");
                return;
            }

            Util.sendMsg(duelSender,ChatColor.GREEN + "You have sent a duel request to " + ChatColor.AQUA + duelTargetName + ".");
            Util.sendMsg(duelTarget,ChatColor.translateAlternateColorCodes('&',"&aYou have been sent a duel request from &b" + duelSenderName));
            Util.sendEmptyMsg(duelTarget,ChatColor.translateAlternateColorCodes('&',"&ause &b/duel accept "+duelSenderName+"&a, to accept the request."));
            this.duelRequests.put(duelSenderName,duelTargetName);
        } else {
            Util.sendMsg(duelSender, ChatColor.AQUA+ duelTargetIn + ChatColor.RED + " is not online! Did you type it correctly?");
        }

    }

    /**
     * handles accepting the request with the specified player to accept the duel request
     * @param accepter the player that is accepting the request
     * @param senderIn the string player of whom they are accepting
     */
    public void acceptRequest(Player accepter,String senderIn){

        if(this.duelRequests.containsKey(senderIn) && this.duelRequests.containsValue(accepter.getName())){
          Player sender = Bukkit.getPlayer(senderIn);
          if(sender != null){
            this.duelRequests.remove(senderIn);

            this.startDuel(accepter,sender);
            return;
          } else {
            Util.sendMsg(accepter,ChatColor.YELLOW+"Duel sender "+ senderIn +" has gone offline!, duel cancelled!");
          }
          this.duelRequests.remove(senderIn);
          return;
        } else {
            Util.sendMsg(accepter,ChatColor.RED+"You do not have any duel requests from "+ ChatColor.AQUA + senderIn +".");
            return;
        }

    }

    /**
     * attempt to start the duel with the two players
     * @param accepter the player that accepted the request
     * @param sender the player that sent the reqest
     */
    public void startDuel(Player accepter, Player sender) {
        String accepterName = accepter.getName();//the duel accepter name
        String senderName = sender.getName();//the duel request sender name
        List<DuelArena> arenas = this.getDuelArenas();//list of arenas
        FileManager fm = plugin.getFileManager();//file manager instance
        ItemManager im = plugin.getItemManager();//item manager instance

       if(arenas.size() <= 0){//if there are no arenas stop the duel
         Util.sendMsg(sender , Util.NO_ARENAS);
         Util.sendMsg(accepter , Util.NO_ARENAS);
         return;
       }
        for(DuelArena a: arenas){
            if(a.getDuelState() == DuelState.WAITING){
              a.setDuelState(DuelState.STARTING);//set the duel state to starting
              if(fm.isDuelStartAnnouncementEnabled()) {
                    Util.broadcastMessage(ChatColor.GREEN + "A duel is Starting between " +
                            ChatColor.AQUA + accepterName +
                            ChatColor.GREEN + " and " +
                            ChatColor.AQUA + senderName);
              }
              a.addPlayer(accepterName);//add the players to the arena
              a.addPlayer(senderName);

              if(fm.isUsingSeperateInventories()) {//store the players inventories
                  if(plugin.isDebugEnabled()){
                      SendConsoleMessage.debug("Storing inventories enabled. storing player inventories");
                  }
                  this.storeInventory(accepter);
                  this.storeInventory(sender);
              }

              accepter.teleport(this.generateRandomLocation(a));//teleport the players to a random location in the duel arena
              sender.teleport(this.generateRandomLocation(a));

              frozenPlayers.add(accepter.getName());//freeze the players
              frozenPlayers.add(sender.getName());

              if(fm.isUsingSeperateInventories()) {
                  if(plugin.isDebugEnabled()){
                      SendConsoleMessage.debug("Storing inventories enabled, giving duel items.");
                  }
                    im.givePlayerDuelItems(accepter);
                    im.givePlayerDuelItems(sender);
              }

                /**
                 * start the duel with the two players and the arena they are in
                 */
              new StartDuelThread(plugin, sender, accepter, a).runTaskTimer(plugin ,20L ,20L );
              return;
            }
        }
        Util.sendMsg(accepter,ChatColor.YELLOW+"There are no free duel arenas, please try again later!");
        Util.sendMsg(sender,ChatColor.YELLOW+"There are no free duel arenas, please try again later!");
    }

    /**
     * Generates a random point between two other points.
     *
     * @param arg0 Point 1.
     * @param arg1 Point 2.
     * @return A random point.
     */
    private double randomGenRange(double arg0, double arg1) {
        double range = (arg0 < arg1) ? arg1 - arg0 : arg0 - arg1;
        if (range < 1)
            return Math.floor(arg0) + 0.5d;
        double min = (arg0 < arg1) ? arg0 : arg1;
        return Math.floor(min + (Math.random() * range)) + 0.5d;
    }

    /**
     * Generates a random location in a duelarena
     *
     * @param a The arena.
     * @return Random location.
     */
    private Location generateRandomLocation(DuelArena a) {
        double x, y, z;
        World w = a.getPos1().getWorld();
        x = randomGenRange(a.getPos1().getX(), a.getPos2().getX());
        y = randomGenRange(a.getPos1().getY(), a.getPos2().getY());
        z = randomGenRange(a.getPos1().getZ(), a.getPos2().getZ());

        return new Location(w, x, y + 0.5, z);
    }

    /**
     * remove a duel arena
     * @param daIn the duel arena
     */
    public void removeDuelArena(DuelArena daIn){
        for(DuelArena da: this.getDuelArenas() ){
            if(da == daIn){
                this.duelArenas.remove(daIn);
                return;
            }
        }
    }

    /**
     * add a dead player
     * @param playerName the players name
     */
    public void addDeadPlayer(String playerName){
        this.deadPlayers.add(playerName);
    }

    public List<String> getDeadPlayers(){
        return this.deadPlayers;
    }

    /**
     * remove a dead player
     * @param playerName the players name
     */
    public void removedDeadPlayer(String playerName){
        this.deadPlayers.remove(playerName);
    }

    /**
     * check to see if a player is dead (after being killed in a duel)
     * @param playerName the players name
     * @return true if they are a dead player, false if not
     */
    public boolean isDeadPlayer(String playerName){
        if(this.getDeadPlayers().contains(playerName)){
            return true;
        }
        return false;
    }

    /**
     * Method to store a players inventory
     * @param p the player to store the inventory of
     */
    public static void storeInventory(Player p) {
        ItemStack[] inv = p.getInventory().getContents();
        ItemStack[] arm = p.getInventory().getArmorContents();
        inventories.put(p.getName(), inv);
        armour.put(p.getName(), arm);
        p.getInventory().clear(-1, -1);
        Util.sendMsg(p,ChatColor.GREEN + "Your inventory has been stored and will be restored after the Duel.");
    }

    /**
     * Method to restore a players inventory
     * @param p the player to restore the inventory to
     */
    public static void restoreInventory(Player p) {
        p.getInventory().clear(-1, -1);// clear their inventory completely
        if (inventories.containsKey(p.getName()) && armour.containsKey(p.getName())) {
            p.getInventory().setContents(inventories.get(p.getName()));
            p.getInventory().setArmorContents(armour.get(p.getName()));
            inventories.remove(p.getName());
            armour.remove(p.getName());
            Util.sendMsg(p,ChatColor.GREEN + "Your inventory has been restored.");
        } else {
            Util.sendMsg(p,ChatColor.RED + "There was an error restoring your inventory!");
        }
    }

    /**
     * end a duel by passing in a player.
     * this would be used for if a player dies,
     * leaves the game or leaves a duel by command
     * @param player the player
     */
    public void endDuel(Player player) {
        FileManager fm = plugin.getFileManager();
        ItemManager im = plugin.getItemManager();
        String playerName = player.getName();

        DuelArena arena = this.getPlayersArena(playerName);
        arena.removePlayer(playerName);
        player.teleport(fm.getLobbySpawnLocation());
        if(plugin.isUsingSeperatedInventories()) {
            this.restoreInventory(player);
        }

        if(arena.getPlayers().size() == 1){
            im.rewardPlayer(arena);
        }

    }

    /**
     * end a duel by duelarena
     * player is rewarded only if there is one left
     * otherwise both players get nothing
     * @param arena the arena to be ended
     */
    public void endDuel(DuelArena arena) {
        ItemManager im = plugin.getItemManager();
        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();

        if(arena.getPlayers().size() == 1){
            im.rewardPlayer(arena);
            return;
        }

        for(String player: arena.getPlayers()) {
            if(isFrozen(player)) {// if player is frozen
                removeFrozenPlayer(player);//remove frozen player
            }
            Player playerOut = Bukkit.getPlayer(player);
            if(playerOut != null) {
                playerOut.teleport(fm.getLobbySpawnLocation());//teleport them to the lobby spawn
                if(plugin.isUsingSeperatedInventories()) {
                    dm.restoreInventory(playerOut);//restore their inventory
                }
                Util.sendMsg(playerOut,ChatColor.RED + "Duel was forcefully cancelled!");
            }
            arena.getPlayers().remove(player);//remove the player
        }
        arena.getPlayers().clear();
        arena.setDuelState(DuelState.WAITING);
    }
}
