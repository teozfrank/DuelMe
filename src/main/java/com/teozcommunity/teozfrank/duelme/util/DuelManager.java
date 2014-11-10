package com.teozcommunity.teozfrank.duelme.util;

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

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.threads.StartDuelThread;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DuelManager {

    private DuelMe plugin;

    /**
     * map to keep track of the dueling requests
     * the key is the duel sender
     * the value is the player who has been sent a request
     */
    public Map<UUID, UUID> duelRequests;

    public Map<UUID, Double> betRequests;

    /**
     * list to hold the current spectating player uuids
     */
    public List<UUID> spectatingPlayerUUIDs;

    /**
     * list to hold the frozen player uuids (before a duel starts)
     */
    public List<UUID> frozenPlayerUUIDs;

    /**
     * list to hold the dead players
     */
    public List<UUID> deadPlayerUUIDs;

    /**
     * list to hold arena objects
     */
    public List<DuelArena> duelArenas;

    private List<PlayerData> playerData;

    public DuelManager(DuelMe plugin) {
        this.plugin = plugin;
        this.duelRequests = new HashMap<UUID, UUID>();
        this.spectatingPlayerUUIDs = new ArrayList<UUID>();
        this.frozenPlayerUUIDs = new ArrayList<UUID>();
        this.duelArenas = new ArrayList<DuelArena>();
        this.deadPlayerUUIDs = new ArrayList<UUID>();
        this.playerData = new ArrayList<PlayerData>();
        this.betRequests = new HashMap<UUID, Double>();
    }

    /**
     * gets a list of the arena objects
     *
     * @return list of arenas
     */
    public List<DuelArena> getDuelArenas() {
        return duelArenas;
    }

    /**
     * add a duel arena
     *
     * @param da the duel arena
     */
    public void addDuelArena(DuelArena da) {
        this.duelArenas.add(da);
    }

    /**
     * get a duel arena by name
     *
     * @param duelArenaName the duel arena name
     * @return the duel arena , null if it does not exist
     */
    public DuelArena getDuelArenaByName(String duelArenaName) {
        for (DuelArena da : duelArenas) {
            if (da.getName().equalsIgnoreCase(duelArenaName)) {
                return da;
            }
        }
        return null;
    }


    /**
     * if a player is in a duel
     *
     * @param playerUUIDIn the players UUID
     * @return true if is in a duel, false if not
     */
    public boolean isInDuel(UUID playerUUIDIn) {
        for (DuelArena a : this.getDuelArenas()) {
            if (a.getPlayers().contains(playerUUIDIn)) {
                return true;
            }
        }
        return false;
    }

    /**
     * get the arena name that a player is in
     *
     * @param playerUUIDIn the players name
     * @return the arena name that the plater is in,
     * returns null if the player is not in an arena
     */
    public String getPlayersArenaName(UUID playerUUIDIn) {
        for (DuelArena a : this.getDuelArenas()) {
            if (a.getPlayers().contains(playerUUIDIn)) {
                return a.getName();
            }
        }
        return null;
    }

    /**
     * gets the arena of two players
     *
     * @param player1UUID the first player
     * @param player2UUID the second player
     * @return the arena that the players are in
     * , null if both players are not in the same arena.
     */
    public DuelArena getPlayersArena(UUID player1UUID, UUID player2UUID) {

        for (DuelArena a : this.getDuelArenas()) {
            List<UUID> players = a.getPlayers();
            if (players.contains(player1UUID) && players.contains(player2UUID)) {
                return a;
            }
        }
        return null;
    }

    public boolean isFrozen(UUID playerUUIDIn) {
        if (this.getFrozenPlayerUUIDs().contains(playerUUIDIn)) {
            return true;
        }
        return false;
    }

    /**
     * get a list of the frozen players
     *
     * @return list of frozen players
     */
    public List<UUID> getFrozenPlayerUUIDs() {
        return this.frozenPlayerUUIDs;
    }

    /**
     * add a frozen player to stop them from moving
     *
     * @param playerUUID the players name
     */
    public void addFrozenPlayer(UUID playerUUID) {
        if(plugin.isDebugEnabled()) {
            SendConsoleMessage.debug("frozen player added: " + playerUUID);
        }
        this.frozenPlayerUUIDs.add(playerUUID);
    }

    /**
     * add a frozen players to stop them from moving
     *
     * @param senderUUID the duel sender
     * @param targetUUID the duel target
     */
    public void addFrozenPlayer(UUID senderUUID, UUID targetUUID) {
        if(plugin.isDebugEnabled()) {
            SendConsoleMessage.debug("frozen sender added: " + senderUUID);
            SendConsoleMessage.debug("frozen target added: " + targetUUID);
        }
        this.frozenPlayerUUIDs.add(senderUUID);
        this.frozenPlayerUUIDs.add(targetUUID);
    }

    public List<PlayerData> getPlayerData() {
        return playerData;
    }

    public void setPlayerData(List<PlayerData> playerData) {
        this.playerData = playerData;
    }

    /**
     * remove a frozen player allowing them to move
     *
     * @param playerUUIDIn the players name
     */
    public void removeFrozenPlayer(UUID playerUUIDIn) {
        this.frozenPlayerUUIDs.remove(playerUUIDIn);
    }

    /**
     * gets the arena of a player
     *
     * @param playerUUID the players UUID
     * @return the arena of the player, null if the player
     * is not in a arena
     */
    public DuelArena getPlayersArenaByUUID(UUID playerUUID) {

        for (DuelArena a : this.getDuelArenas()) {
            List<UUID> players = a.getPlayers();
            if (players.contains(playerUUID)) {
                return a;
            }
        }
        return null;
    }

    public boolean hasSentDuelWithBet(UUID senderUUID) {
        if (this.betRequests.containsKey(senderUUID)) {
            return true;
        }
        return false;
    }

    /**
     * handle normal duel requests
     *
     * @param duelSender   the sender of the request
     * @param duelTargetIn the string player of the target player
     */
    public void sendNormalDuelRequest(Player duelSender, String duelTargetIn) {

        String duelSenderName = duelSender.getName();
        UUID duelSenderUUID = duelSender.getUniqueId();
        Player duelTarget = Bukkit.getPlayer(duelTargetIn);

        if (duelTarget != null) {

            UUID duelTargetUUID = duelTarget.getUniqueId();

            if (this.duelRequests.containsKey(duelSenderUUID) && this.duelRequests.containsValue(duelTargetUUID)) {
                Util.sendMsg(duelSender, ChatColor.YELLOW + "You have already sent a request to " +
                        ChatColor.AQUA + duelTargetIn + ".");
                return;
            }

            String duelTargetName = duelTarget.getName();
            if (!plugin.isDebugEnabled()) {//to test plugin with one player
                if (duelSenderName == duelTargetName) {
                    Util.sendMsg(duelSender, ChatColor.RED + "You cannot duel yourself!");
                    return;
                }
            }

            Util.sendMsg(duelSender, ChatColor.GREEN + "You have sent a duel request to " + ChatColor.AQUA + duelTargetName + ".");
            Util.sendMsg(duelTarget, ChatColor.translateAlternateColorCodes('&', "&aYou have been sent a duel request from &b" + duelSenderName));
            Util.sendEmptyMsg(duelTarget, ChatColor.translateAlternateColorCodes('&', "&ause &b/duel accept " + duelSenderName + "&a, to accept the request."));
            this.duelRequests.put(duelSenderUUID, duelTargetUUID);
        } else {
            Util.sendMsg(duelSender, ChatColor.AQUA + duelTargetIn + ChatColor.RED + " is not online! Did you type it correctly?");
        }

    }

    /**
     * handle duel requests
     *
     * @param duelSender   the sender of the request
     * @param duelTargetIn the string player of the target player
     * @param amount       the duel bet amount
     */
    public void sendBetDuelRequest(Player duelSender, String duelTargetIn, double amount) {

        String duelSenderName = duelSender.getName();
        UUID duelSenderUUID = duelSender.getUniqueId();
        FileManager fm = plugin.getFileManager();
        double minBetAmount = fm.getMinBetAmount();

        Player duelTarget = Bukkit.getPlayer(duelTargetIn);

        if (duelTarget != null) {

            UUID duelTargetUUID = duelTarget.getUniqueId();


            if (this.duelRequests.containsKey(duelSenderUUID) && this.duelRequests.containsValue(duelTargetUUID)) {
                Util.sendMsg(duelSender, ChatColor.YELLOW + "You have already sent a request to " +
                        ChatColor.AQUA + duelTargetIn + ".");
                return;
            }

            String duelTargetName = duelTarget.getName();
            if (!plugin.isDebugEnabled()) {//to test plugin with one player
                if (duelSenderName == duelTargetName) {
                    Util.sendMsg(duelSender, ChatColor.RED + "You cannot duel yourself!");
                    return;
                }
            }

            if (fm.getMinBetAmount() >= amount) {
                Util.sendMsg(duelSender, "You must provide a bet amount that is greater than " + fm.getMinBetAmount());
                return;
            }

            if (!this.hasEnoughMoney(duelSenderName, amount)) {
                Util.sendMsg(duelSender, ChatColor.RED + "You do not have enough money to duel for this bet amount!");
                return;
            }

            Util.sendMsg(duelSender, ChatColor.GREEN + "You have sent a duel request to " + ChatColor.AQUA +
                    duelTargetName + ChatColor.GREEN + " for a bet amount of " + ChatColor.GREEN + amount);
            Util.sendMsg(duelTarget, ChatColor.translateAlternateColorCodes('&', "&aYou have been sent a duel request from &b" + duelSenderName +
                    " &afor a bet amount of &b" + amount));
            Util.sendEmptyMsg(duelTarget, ChatColor.translateAlternateColorCodes('&', "&ause &b/duel accept " + duelSenderName + "&a, to accept the request."));
            this.duelRequests.put(duelSenderUUID, duelTargetUUID);
            this.betRequests.put(duelSenderUUID, amount);
        } else {
            Util.sendMsg(duelSender, ChatColor.AQUA + duelTargetIn + ChatColor.RED + " is not online! Did you type it correctly?");
        }

    }


    /**
     * handles accepting the request with the specified player to accept the duel request
     *
     * @param accepter the player that is accepting the request
     * @param senderIn the string player of whom they are accepting
     */
    public void acceptRequest(Player accepter, String senderIn) {

        UUID accepterUUID = accepter.getUniqueId();
        Player sender = Bukkit.getPlayer(senderIn);

        if (sender == null) {
            Util.sendMsg(accepter, ChatColor.AQUA + senderIn + ChatColor.RED + " is not online! Did you type it correctly?");
            return;
        }

        UUID senderUUID = sender.getUniqueId();

        if (this.duelRequests.containsKey(senderUUID) && this.duelRequests.containsValue(accepterUUID)) {
            this.duelRequests.remove(senderUUID);
            if (this.hasSentDuelWithBet(senderUUID)) {
                double betAmount = this.betRequests.get(senderUUID);
                this.startDuel(accepter, sender, betAmount);
                this.betRequests.remove(senderUUID);
            } else {
                this.startDuel(accepter, sender, 0);
            }

            return;
        } else {
            Util.sendMsg(accepter, ChatColor.RED +
                    "You do not have any duel requests from " + ChatColor.AQUA + senderIn + ".");
        }

    }

    /**
     * attempt to start the duel with the two players
     *
     * @param accepter the player that accepted the request
     * @param sender   the player that sent the reqest
     */
    public void startDuel(Player accepter, Player sender, double betAmount) {

        String accepterName = accepter.getName();//the duel accepter name
        String senderName = sender.getName();//the duel request sender name
        double totalBetAmount = betAmount * 2;

        UUID accepterUUID = accepter.getUniqueId();
        UUID senderUUID = sender.getUniqueId();

        List<DuelArena> arenas = this.getDuelArenas();//list of arenas
        FileManager fm = plugin.getFileManager();//file manager instance
        ItemManager im = plugin.getItemManager();//item manager instance

        if (arenas.size() <= 0) {//if there are no arenas stop the duel
            Util.sendMsg(sender, Util.NO_ARENAS);
            Util.sendMsg(accepter, Util.NO_ARENAS);
            return;
        }
        for (DuelArena a : arenas) {
            if (a.getDuelState() == DuelState.WAITING) {
                a.setDuelState(DuelState.STARTING);//set the duel state to starting
                if (fm.isDuelStartAnnouncementEnabled()) {
                    Util.broadcastMessage(ChatColor.GREEN + "A duel is Starting between " +
                            ChatColor.AQUA + accepterName +
                            ChatColor.GREEN + " and " +
                            ChatColor.AQUA + senderName);
                }

                if (betAmount > 0) {
                    a.setHasBet(true);
                    plugin.getEconomy().withdrawPlayer(senderName, betAmount);
                    plugin.getEconomy().withdrawPlayer(accepterName, betAmount);
                    Util.sendMsg(sender, accepter,
                            ChatColor.GREEN + "You have been charged a bet amount of " + ChatColor.AQUA + betAmount);
                    Util.sendMsg(sender, accepter, "The winner of this duel will win a total bet amount of " +
                            ChatColor.AQUA + totalBetAmount);
                    a.setBetAmount(totalBetAmount);
                }

                a.addPlayerUUID(accepterUUID);//add the players to the arena
                a.addPlayerUUID(senderUUID);


                this.storePlayerData(accepter);
                this.storePlayerData(sender);


                if (a.getSpawnpoint1() != null && a.getSpawnpoint2() != null) {
                    if (plugin.isDebugEnabled()) {
                        SendConsoleMessage.debug("Spawnpoints for arena set teleporting players to locations.");
                    }
                    accepter.teleport(a.getSpawnpoint1());//teleport the players to set spawn location in the duel arena
                    sender.teleport(a.getSpawnpoint2());
                } else {
                    if (plugin.isDebugEnabled()) {
                        SendConsoleMessage.debug("Spawnpoints for arena not set falling back to random spawn locations.");
                    }
                    accepter.teleport(this.generateRandomLocation(a));//teleport the players to a random location in the duel arena
                    sender.teleport(this.generateRandomLocation(a));
                }

                frozenPlayerUUIDs.add(accepterUUID);//freeze the players
                frozenPlayerUUIDs.add(senderUUID);

                if (fm.isUsingSeperateInventories()) {
                    if (plugin.isDebugEnabled()) {
                        SendConsoleMessage.debug("Storing inventories enabled, giving duel items.");
                    }
                    im.givePlayerDuelItems(accepter);
                    im.givePlayerDuelItems(sender);
                }

                new StartDuelThread(plugin, sender, accepter, a).runTaskTimer(plugin, 20L, 20L);
                return;
            }
        }
        Util.sendMsg(accepter, ChatColor.YELLOW + "There are no free duel arenas, please try again later!");
        Util.sendMsg(sender, ChatColor.YELLOW + "There are no free duel arenas, please try again later!");
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
     *
     * @param daIn the duel arena
     */
    public void removeDuelArena(DuelArena daIn) {
        for (DuelArena da : this.getDuelArenas()) {
            if (da == daIn) {
                this.duelArenas.remove(daIn);
                return;
            }
        }
    }

    /**
     * add a dead player
     *
     * @param playerUUID the players name
     */
    public void addDeadPlayer(UUID playerUUID) {
        this.deadPlayerUUIDs.add(playerUUID);
    }

    public List<UUID> getDeadPlayers() {
        return this.deadPlayerUUIDs;
    }

    /**
     * remove a dead player
     *
     * @param playerUUID the players unique ID
     */
    public void removedDeadPlayer(UUID playerUUID) {
        this.deadPlayerUUIDs.remove(playerUUID);
    }

    /**
     * check to see if a player is dead (after being killed in a duel)
     *
     * @param playerUUID the players unique ID
     * @return true if they are a dead player, false if not
     */
    public boolean isDeadPlayer(UUID playerUUID) {
        if (this.getDeadPlayers().contains(playerUUID)) {
            return true;
        }
        return false;
    }

    /**
     * get a players data by UUID
     *
     * @param playerUUIDIn the players UUID
     * @return the player data
     */
    public PlayerData getPlayerDataByUUID(UUID playerUUIDIn) {
        for (PlayerData playerDataIn : this.getPlayerData()) {
            if (playerDataIn.getUUID() == playerUUIDIn) {
                return playerDataIn;
            }
        }
        return null;
    }

    /**
     * Method to store a players data
     *
     * @param player the player to store data of
     */
    public void storePlayerData(Player player) {
        UUID playerUUID = player.getUniqueId();
        ItemStack[] arm = player.getInventory().getArmorContents();
        ItemStack[] inv = player.getInventory().getContents();
        Location loc = player.getLocation();
        Float saturation = player.getSaturation();
        int foodLevel = player.getFoodLevel();
        int expLevel = player.getLevel();
        double health = player.getHealth();

        this.playerData.add(new PlayerData(playerUUID, arm, inv, loc, saturation, foodLevel, expLevel, health));

        player.getInventory().clear(-1, -1);
        Util.sendMsg(player, ChatColor.GREEN + "Your player data has been stored and will be restored after the Duel.");
    }

    /**
     * Method to restore a players data
     *
     * @param player the player to restore the data to
     */
    public void restorePlayerData(Player player) {
        UUID playerUUID = player.getUniqueId();
        PlayerData playerData = this.getPlayerDataByUUID(playerUUID);

        player.getInventory().clear(-1, -1);// clear their inventory completely

        try {
            ItemStack[] arm = playerData.getArmour();
            ItemStack[] inv = playerData.getInventory();
            Location loc = playerData.getLocaton();
            Float saturation = playerData.getSaturation();
            int foodLevel = playerData.getFoodLevel();
            int expLevel = playerData.getEXPLevel();
            double health = playerData.getHealth();

            if (!this.isDeadPlayer(playerUUID)) {
                player.teleport(loc);
            }

            if (plugin.isUsingSeperatedInventories()) {
                player.getInventory().setContents(inv);
                player.getInventory().setArmorContents(arm);
            }
            player.setSaturation(saturation);
            player.setFoodLevel(foodLevel);
            player.setLevel(expLevel);
            player.setHealth(health);
            Util.sendMsg(player, ChatColor.GREEN + "Your player data has been restored!");
        } catch (Exception e) {
            Util.sendMsg(player, ChatColor.RED + "There was an error restoring your player data!");
        }


    }

    /**
     * end a duel by passing in a player.
     * this would be used for if a player dies,
     * leaves the game or leaves a duel by command
     *
     * @param player the player
     */
    public void endDuel(Player player) {
        if(plugin.isDebugEnabled()) {
            SendConsoleMessage.debug("End duel.");
        }
        ItemManager im = plugin.getItemManager();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();

        DuelArena arena = this.getPlayersArenaByUUID(playerUUID);
        arena.removePlayer(playerUUID);

        if(player.isDead()) {
            this.addDeadPlayer(playerUUID);
        } else {
            this.restorePlayerData(player);
        }


        if (arena.getPlayers().size() == 1) {
            im.rewardPlayer(arena);
        }


    }

    /**
     * end a duel by duelarena
     * player is rewarded only if there is one left
     * otherwise both players get nothing
     *
     * @param arena the arena to be ended
     */
    public void endDuel(DuelArena arena) {
        if(plugin.isDebugEnabled()) {
            SendConsoleMessage.debug("End duel by duel arena.");
        }
        ItemManager im = plugin.getItemManager();
        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();

        if (arena.getPlayers().size() == 1) {
            im.rewardPlayer(arena);
            return;
        }

        for (UUID playerUUID : arena.getPlayers()) {
            if (isFrozen(playerUUID)) {// if player is frozen
                removeFrozenPlayer(playerUUID);//remove frozen player
            }
            Player playerOut = Bukkit.getPlayer(playerUUID);
            if (playerOut != null) {
                String playerName = playerOut.getName();
                this.restorePlayerData(playerOut);
                Util.sendMsg(playerOut, ChatColor.RED + "Duel was forcefully cancelled!");
                if(arena.hasBet()) {
                    double betAmount = arena.getBetAmount();
                    double refundAmount = betAmount / 2;
                    plugin.getEconomy().depositPlayer(playerName, refundAmount);
                    Util.sendMsg(playerOut, "You have been refunded the amount of " + refundAmount);
                }
            }
            arena.getPlayers().remove(playerUUID);//remove the player
        }

        this.resetArena(arena);
    }

    /**
     * check to see if the player has enough money
     *
     * @param player the player
     * @param amount the amount being withdrawn
     * @return true if they have enough, false if not
     */
    public boolean hasEnoughMoney(String player, double amount) {
        if (plugin.getEconomy().getBalance(player) >= amount) {
            return true;
        }
        return false;
    }

    /**
     * reset a duel arena to initial state
     * @param arena the duel arena
     */
    public void resetArena(DuelArena arena) {
        if(plugin.isDebugEnabled()) {
            SendConsoleMessage.debug("resetting arena.");
        }
        arena.setHasBet(false);
        arena.setBetAmount(0);
        arena.getPlayers().clear();
        arena.setDuelState(DuelState.WAITING);
    }
}
