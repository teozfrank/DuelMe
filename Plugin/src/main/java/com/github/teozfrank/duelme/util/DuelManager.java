package com.github.teozfrank.duelme.util;

/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2014 teozfrank
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import com.github.teozfrank.duelme.main.DuelMe;
import com.github.teozfrank.duelme.threads.DuelStartThread;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class DuelManager {

    private DuelMe plugin;

    private List<DuelRequest> duelRequests;

    /**
     * list to hold the current spectating player uuids
     */
    private List<UUID> spectatingPlayerUUIDs;

    /**
     * list of queued players
     */
    private List<UUID> queuedPlayerUUIDs;

    /**
     * list to hold the frozen player uuids (before a duel starts)
     */
    private List<UUID> frozenPlayerUUIDs;

    /**
     * list of dead players
     */
    private List<UUID> deadPlayers;

    /**
     * list to hold arena objects
     */
    private List<DuelArena> duelArenas;

    private HashMap<UUID, PlayerData> playerData;

    private MessageManager mm;

    public DuelManager(DuelMe plugin) {
        this.plugin = plugin;
        this.duelRequests = new ArrayList<DuelRequest>();
        this.spectatingPlayerUUIDs = new ArrayList<UUID>();
        this.frozenPlayerUUIDs = new ArrayList<UUID>();
        this.duelArenas = new ArrayList<DuelArena>();
        this.playerData = new HashMap<UUID, PlayerData>();
        this.deadPlayers = new ArrayList<UUID>();
        this.mm = plugin.getMessageManager();
        this.queuedPlayerUUIDs = new ArrayList<UUID>();
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
            for(UUID duelPlayerUUID: a.getPlayers()) {
                if(playerUUIDIn.equals(duelPlayerUUID)) {
                    return true;
                }
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
     * add a dead player to the list of dead players
     * @param uuid the uuid of the dead player
     */
    public void addDeadPlayer(UUID uuid) {
        if (!this.deadPlayers.contains(uuid)) {
            this.deadPlayers.add(uuid);
        }
    }

    /**
     * is a player a dead player
     * @param uuid the uuid of the player
     * @return true if dead, false if not
     */
    public boolean isDeadPlayer(UUID uuid) {
        if (getDeadPlayers().contains(uuid)) {
            if (plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("UUID " + uuid + " is in dead player list");
            }
            return true;
        }
        return false;
    }

    public List<UUID> getDeadPlayers() {
        return this.deadPlayers;
    }

    /**
     * remove a dead player from the list of dead players
     * @param uuid the uuid of the dead player to remove
     */
    public void removeDeadPlayer(UUID uuid) {
        this.deadPlayers.remove(uuid);
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
        if (plugin.isDebugEnabled()) {
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
        if (plugin.isDebugEnabled()) {
            SendConsoleMessage.debug("frozen sender added: " + senderUUID);
            SendConsoleMessage.debug("frozen target added: " + targetUUID);
        }
        this.frozenPlayerUUIDs.add(senderUUID);
        this.frozenPlayerUUIDs.add(targetUUID);
    }

    public HashMap<UUID, PlayerData> getPlayerData() {
        return playerData;
    }

    public void setPlayerData(HashMap<UUID, PlayerData> playerData) {
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

    /**
     * check if a player has sent a duel request to a player before
     * @param sender the sender
     * @param target the target player
     * @return true if the player has send a duel request to a given player, false if not
     */
    public boolean hasSentRequest(UUID sender, UUID target) {
        for(DuelRequest duelRequest: duelRequests) {
            if(duelRequest.getDuelSender() == sender && duelRequest.getDuelTarget() == target) {
                return true;
            }
        }
        return false;
    }

    public DuelRequest getDuelRequest(UUID sender, UUID target) {
        for(DuelRequest duelRequest: duelRequests) {
            if(duelRequest.getDuelSender() == sender && duelRequest.getDuelTarget() == target) {
                return duelRequest;
            }
        }
        return null;
    }

    /**
     * handle normal duel requests
     *
     * @param duelSender   the sender of the request
     * @param duelTargetIn the string player of the target player
     */
    public void sendDuelRequest(Player duelSender, String duelTargetIn, String arenaIn) {

        FileManager fm = plugin.getFileManager();
        String duelSenderName = duelSender.getName();
        UUID duelSenderUUID = duelSender.getUniqueId();
        Player duelTarget = Bukkit.getPlayer(duelTargetIn);

        if(arenaIn != null) {
            DuelArena arena = this.getDuelArenaByName(arenaIn);
            if(arena == null) {
                Util.sendMsg(duelSender, ChatColor.RED + "Sorry but that duel arena name you specified doesn't exist.");
                return;
            }
            if(arena.getDuelState() != DuelState.WAITING) {
                Util.sendMsg(duelSender, ChatColor. RED + "Sorry but that duel arena isn't available right now please try another one.");
                return;
            }
        }

        if (duelTarget != null) {

            UUID duelTargetUUID = duelTarget.getUniqueId();

            if (isInDuel(duelTargetUUID)) {
                String playerAlreadyInDuel = mm.getPlayerAlreadyInDuelMessage();
                playerAlreadyInDuel = playerAlreadyInDuel.replaceAll("%target%", duelTargetIn);
                Util.sendMsg(duelSender, playerAlreadyInDuel);
                return;
            }

            if (hasSentRequest(duelSenderUUID, duelTargetUUID)) {
                String requestAlreadySent = mm.getDuelRequestAlreadySentMessage();
                requestAlreadySent = requestAlreadySent.replaceAll("%target%", duelTargetIn);
                Util.sendMsg(duelSender, requestAlreadySent);
                return;
            }

            String duelTargetName = duelTarget.getName();
            if (duelSenderName.equals(duelTargetName)) {
                Util.sendMsg(duelSender, mm.getCannotDuelSelfMessage());
                return;
            }

            String duelRequestSentMessage = mm.getDuelRequestSentMessage();
            duelRequestSentMessage = duelRequestSentMessage.replaceAll("%target%", duelTargetName);
            Util.sendMsg(duelSender, duelRequestSentMessage);
            if (fm.isGUIMenuEnabled()) {

                SendConsoleMessage.error("This feature is broken and is disabled until it has been fixed! Sent a normal non gui request!");
                //plugin.getAcceptMenu().openNormalDuelAccept(duelSender, duelTarget);
                String duelRequestReceived = mm.getDuelRequestReceivedMessage();
                duelRequestReceived = duelRequestReceived.replaceAll("%sender%", duelSenderName);
                Util.sendMsg(duelTarget, ChatColor.translateAlternateColorCodes('&', duelRequestReceived));
            } else {
                String duelRequestReceived = mm.getDuelRequestReceivedMessage();
                duelRequestReceived = duelRequestReceived.replaceAll("%sender%", duelSenderName);
                Util.sendMsg(duelTarget, ChatColor.translateAlternateColorCodes('&', duelRequestReceived));
            }
            this.duelRequests.add(new DuelRequest(duelSenderUUID, duelTargetUUID, arenaIn, System.currentTimeMillis()));
        } else {
            String targetNotOnline = mm.getTargetNotOnlineMessage();
            targetNotOnline = targetNotOnline.replaceAll("%target%", duelTargetIn);
            Util.sendMsg(duelSender, targetNotOnline);
        }

    }

    public void removeDuelRequest(DuelRequest duelRequest) {
        this.duelRequests.remove(duelRequest);
    }


    /**
     * handles accepting the request with the specified player to accept the duel request
     *
     * @param acceptor the player that is accepting the request
     * @param senderIn the string player of whom they are accepting
     */
    public void acceptRequest(Player acceptor, String senderIn) {

        UUID acceptorUUID = acceptor.getUniqueId();
        Player sender = Bukkit.getPlayer(senderIn);

        if (sender == null) {
            String targetNotOnline = mm.getTargetNotOnlineMessage();
            targetNotOnline = targetNotOnline.replaceAll("%target%", senderIn);
            Util.sendMsg(acceptor, targetNotOnline);
            return;
        }

        UUID senderUUID = sender.getUniqueId();

        if (hasSentRequest(senderUUID, acceptorUUID)) {
            DuelRequest duelRequest = getDuelRequest(senderUUID, acceptorUUID);
            this.startDuel(acceptor, sender, duelRequest.getDuelArena());
            this.removeDuelRequest(duelRequest);
            return;
        } else {
            Util.sendMsg(acceptor, ChatColor.RED +
                    "You do not have any duel requests from " + ChatColor.AQUA + senderIn + ".");
        }

    }


    public boolean isArenaFree(DuelArena duelArena) {
        if(duelArena.getDuelState() == DuelState.WAITING) {
            return true;
        }
        return false;
    }

    /**
     * attempt to start the duel with the two players
     *
     * @param acceptor the player that accepted the request
     * @param sender   the player that sent the reqest
     */
    public boolean startDuel(Player acceptor, Player sender, String arena) {

        String acceptorName = acceptor.getName();//the duel acceptor name
        String senderName = sender.getName();//the duel request sender name

        DuelArena duelArena = null;

        final UUID acceptorUUID = acceptor.getUniqueId();
        final UUID senderUUID = sender.getUniqueId();

        List<DuelArena> arenas = this.getDuelArenas();//list of arenas
        FileManager fm = plugin.getFileManager();//file manager instance
        ItemManager im = plugin.getItemManager();//item manager instance

        if (arenas.size() <= 0) {//if there are no arenas stop the duel
            Util.sendMsg(sender, mm.getNoDuelArenasMessage());
            Util.sendMsg(acceptor, mm.getNoDuelArenasMessage());
            return false;
        }

        if(arena != null) {
            duelArena = getDuelArenaByName(arena);
            if(duelArena == null) {
                Util.sendMsg(acceptor, ChatColor.RED + "The duel arena you requested to duel in does not exist!");
                return false;
            }
            if(!isArenaFree(duelArena)) {
                Util.sendMsg(acceptor, ChatColor.RED + "The duel arena you requested to duel in is not free!");
                return false;
            }
        }

        if(duelArena == null) {
            duelArena = this.getFreeArena();
        }

        if (duelArena == null) {
            Util.sendMsg(acceptor, ChatColor.YELLOW + "There are no free duel arenas, please try again later!");
            Util.sendMsg(sender, ChatColor.YELLOW + "There are no free duel arenas, please try again later!");
            return false;
        }

        duelArena.setDuelState(DuelState.STARTING);//set the duel state to starting
        this.updateDuelStatusSign(duelArena);
        if (fm.isDuelStartAnnouncementEnabled()) {
            String duelStartBroadcast = mm.getDuelStartMessage();
            duelStartBroadcast = duelStartBroadcast.replaceAll("%sender%", senderName);
            duelStartBroadcast = duelStartBroadcast.replaceAll("%acceptor%", acceptorName);
            Util.broadcastMessage(duelStartBroadcast);
        }

        duelArena.addPlayerUUID(acceptorUUID);//add the players to the arena
        duelArena.addPlayerUUID(senderUUID);
        Location spawnpoint1 = duelArena.getSpawnpoint1();
        Location spawnpoint2 = duelArena.getSpawnpoint2();

        surroundLocation(spawnpoint1, Material.valueOf(fm.getDuelSurroundMaterial()));
        surroundLocation(spawnpoint2, Material.valueOf(fm.getDuelSurroundMaterial()));

        this.storePlayerData(acceptor);
        this.storePlayerData(sender);

        if (duelArena.getSpawnpoint1() != null && duelArena.getSpawnpoint2() != null) {
            if (plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("Spawnpoints for arena set teleporting players to locations.");
            }

            removePotionEffects(acceptor);//remove players active potion effects
            removePotionEffects(sender);
            acceptor.teleport(duelArena.getSpawnpoint1());//teleport the players to set spawn location in the duel arena
            sender.teleport(duelArena.getSpawnpoint2());

        } else {
            if (plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("Spawnpoints for arena not set falling back to random spawn locations.");
            }
            acceptor.teleport(this.generateRandomLocation(duelArena));//teleport the players to a random location in the duel arena
            sender.teleport(this.generateRandomLocation(duelArena));
        }

        if (fm.isUsingSeperateInventories()) {
            if (plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("Storing inventories enabled, giving duel items.");
            }
            im.givePlayerDuelItems(acceptor);
            im.givePlayerDuelItems(sender);
        }


        new DuelStartThread(plugin, sender, acceptor, duelArena).runTaskTimer(plugin, 20L, 20L);
        return true;
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
     * get a players data by UUID
     *
     * @param playerUUIDIn the players UUID
     * @return the player data
     */
    public PlayerData getPlayerDataByUUID(UUID playerUUIDIn) {
        return playerData.get(playerUUIDIn);
    }

    public void addPlayerData(UUID uuidIn, PlayerData playerData) {
        this.getPlayerData().put(uuidIn, playerData);
    }

    public void removePlayerDataByUUID(UUID playerUUIDIn) {
        this.getPlayerData().remove(playerUUIDIn);
    }

    /**
     * Method to store a players data
     *
     * @param player the player to store data of
     */
    public void storePlayerData(Player player) {
        FileManager fm = plugin.getFileManager();
        UUID playerUUID = player.getUniqueId();
        ItemStack[] arm = player.getInventory().getArmorContents();
        ItemStack[] inv = player.getInventory().getContents();
        Location loc = player.getLocation();
        Float saturation = player.getSaturation();
        int foodLevel = player.getFoodLevel();
        int expLevel = player.getLevel();
        double health = player.getHealth();
        GameMode gameMode = player.getGameMode();
        boolean allowedFlight = player.getAllowFlight();
        if(allowedFlight) {
            player.setAllowFlight(false);
        }
        if (plugin.isDebugEnabled()) {
            SendConsoleMessage.info("Player location for player: " + player.getName() + ":" + loc);
        }
        if (player.getGameMode() != GameMode.SURVIVAL) {
            player.setGameMode(GameMode.SURVIVAL);
        }
        this.addPlayerData(playerUUID, new PlayerData(arm, inv, loc, saturation, foodLevel, expLevel, health, gameMode, allowedFlight));

        if (fm.isUsingSeperateInventories()) {
            player.getInventory().clear();
        }
    }

    /**
     * attempt restore a players data with a player object
     *
     * @param player the player to restore the data to
     * @return true if successful, false if not
     */
    public boolean restorePlayerData(Player player) {
        UUID playerUUID = player.getUniqueId();
        PlayerData playerData = this.getPlayerDataByUUID(playerUUID);

        try {
            ItemStack[] arm = playerData.getArmour();
            ItemStack[] inv = playerData.getInventory();
            Location loc = playerData.getLocaton();
            Float saturation = playerData.getSaturation();
            int foodLevel = playerData.getFoodLevel();
            int expLevel = playerData.getEXPLevel();
            double health = playerData.getHealth();
            GameMode gameMode = playerData.getGameMode();
            boolean allowedFlight = playerData.getAllowedFight();

            if (!isDeadPlayer(playerUUID)) {
                if (plugin.isDebugEnabled()) {
                    SendConsoleMessage.debug("Player is not dead, Teleporting: " + player.getName() + " to location:" + loc);
                }
                player.teleport(loc);
            }

            if (plugin.isUsingSeperatedInventories()) {
                player.getInventory().clear();// clear their inventory completely
                player.getInventory().setContents(inv);
                player.getInventory().setArmorContents(arm);
            }
            player.setGameMode(gameMode);
            player.setAllowFlight(allowedFlight);
            player.setSaturation(saturation);
            player.setFoodLevel(foodLevel);
            player.setLevel(expLevel);
            player.setHealth(health);
            removePotionEffects(player);
            this.removePlayerDataByUUID(playerUUID);
            return true;
        } catch (Exception e) {
            Util.sendMsg(player, ChatColor.RED + "There was an error restoring your player data!");
            if (plugin.isDebugEnabled()) {
                SendConsoleMessage.debug(e.getMessage());
            }
            return false;
        }


    }

    /**
     * end a duel by passing in a player.
     * this would be used for if a player dies,
     * leaves the game or leaves a duel by command
     *
     * @param player the losing player
     */
    public void endDuel(Player player) {
        if (plugin.isDebugEnabled()) {
            SendConsoleMessage.debug("End duel by player.");
        }
        ItemManager im = plugin.getItemManager();
        UUID playerUUID = player.getUniqueId();

        DuelArena arena = this.getPlayersArenaByUUID(playerUUID);
        arena.removePlayer(playerUUID);
        if (!player.isDead()) {
            this.restorePlayerData(player);
        }

        if (arena.getPlayers().size() == 1) {
            im.rewardPlayer(arena, player.getName());
        }
    }

    public void removePotionEffects(Player player) {
        int activePotions = 0;
        for (PotionEffect p : player.getActivePotionEffects()) {
            player.removePotionEffect(p.getType());
            activePotions++;
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
        if (plugin.isDebugEnabled()) {
            SendConsoleMessage.debug("End duel by duel arena.");
        }
        ItemManager im = plugin.getItemManager();

        if (plugin.isDebugEnabled()) {
            SendConsoleMessage.debug("Playercount: " + arena.getPlayers().size());
        }

        if (arena.getPlayers().size() == 1) {
            if (plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("One player remains, rewarding.");
            }
            im.rewardPlayer(arena, "");
            return;
        }

        for (UUID playerUUID : arena.getPlayers()) {
            if (plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("Player UUID: " + playerUUID.toString());
            }
            Player playerOut = Bukkit.getPlayer(playerUUID);
            if (playerOut != null) {
                String playerName = playerOut.getName();
                this.restorePlayerData(playerOut);
                Util.sendMsg(playerOut, mm.getDuelForcefullyCancelledMessage());
            }
        }

        this.resetArena(arena);
    }

    /**
     * reset a duel arena to initial state
     * @param arena the duel arena
     */
    public void resetArena(DuelArena arena) {
        if (plugin.isDebugEnabled()) {
            SendConsoleMessage.debug("resetting arena.");
        }
        arena.getPlayers().clear();
        arena.setDuelState(DuelState.WAITING);
        this.updateDuelStatusSign(arena);
    }

    /**
     * update the state of a status sign if there is one for that arena
     * @param arena the arena
     */
    public void updateDuelStatusSign(DuelArena arena) {
        FileManager fm = plugin.getFileManager();
        Location location;
        Block block;

        try {
            location = fm.getArenaStatusSignLocation(arena.getName());
            block = location.getBlock();
        } catch (NullPointerException e) {
            if (plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("No sign set for arena " + arena.getName());
            }
            return;
        }

        if (!block.getType().equals(Material.WALL_SIGN)) {
            return;
        }
        try {
            Sign sign = (Sign) block.getState();
            sign.setLine(2, arena.getDuelState().toString());
            sign.setLine(3, arena.getPlayers().size() + "/2");
            sign.update();
            if (plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("Update duel sign");
            }
        } catch (Exception e) {
            SendConsoleMessage.debug(e.getMessage());
        }

    }

    /**
     * loop through the list of arenas and add the free ones to a new list
     * then return a random free arena
     * @return a random free duel arena, null if none is available
     */
    public DuelArena getFreeArena() {

        Random random = new Random();

        List<DuelArena> freeDuelArenas = new ArrayList<DuelArena>();

        for (DuelArena duelArena : getDuelArenas()) {
            if (duelArena.getDuelState().equals(DuelState.WAITING) && duelArena.getPlayers().size() == 0) {//if the duel arena state is waiting for players and there are no players in the arena.
                if(getDuelArenas().size() > 1) {
                    freeDuelArenas.add(duelArena);
                } else {
                    return duelArena;
                }

            }
        }

        if(freeDuelArenas.isEmpty()) {
            return null;//no free duel arenas
        }

        return freeDuelArenas.get(random.nextInt(freeDuelArenas.size()));
    }

    /**
     * returns a list of locations based on the inputs given
     * credits for original source comes from bukkit forums, here:
     * https://bukkit.org/threads/creating-a-3x3-square-dissapearing-after-time.140159/
     * @param loc the location to surround
     * @param r radius
     * @param h height
     * @param hollow true if hollow false if not
     * @param sphere true of sphere false if not
     * @param plus_y
     * @return
     */
    public List<Location> surround(Location loc, Integer r, Integer h, Boolean hollow, Boolean sphere, int plus_y) {
        List<Location> circleblocks = new ArrayList<Location>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx +r; x++)
            for (int z = cz - r; z <= cz +r; z++)
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r*r && !(hollow && dist < (r-1)*(r-1))) {
                        Location l = new Location(loc.getWorld(), x, y + plus_y, z);
                        if(!(l.getBlockY() == cy)) {
                            circleblocks.add(l);
                        }

                    }
                }

        return circleblocks;
    }

    /**
     * surround a specific location with a given material
     * @param location the location
     * @param material the material to set it to
     */
    public void surroundLocation(Location location, Material material) {
        final List<Location> circs =  surround(location, 2, 2, true, true, 1);
        for (Location loc : circs) {
            loc.getBlock().setType(material);
        }
    }

    public List<DuelRequest> getDuelRequests() {
        return duelRequests;
    }

    /**
     * Get a list of queued players
     * @return a list of queued players
     */
    public List<UUID> getQueuedPlayerUUIDs() {
        return queuedPlayerUUIDs;
    }

    /**
     * Add a player to the queue
     * @param playersUUID the players UUID
     */
    public void addQueuedPlayer(UUID playersUUID) {
        this.queuedPlayerUUIDs.add(playersUUID);
    }

    /**
     * remove a player from the queue
     * @param playersUUID the players uuid
     */
    public void removeQueuedPlayer(UUID playersUUID) {
        this.queuedPlayerUUIDs.remove(playersUUID);
    }

    /**
     * Remove a queued player by index
     * @param index the index to remove from
     */
    public void removeQueuedPlayerByIndex(int index) {
        this.queuedPlayerUUIDs.remove(index);
    }

    /**
     * check if a player is currently in the queue
     * @param playersUUID the players uuid
     * @return true if the player is queued false if not
     */
    public boolean isQueued(UUID playersUUID) {
        return this.queuedPlayerUUIDs.contains(playersUUID);
    }

    /**
     * Set the queued players uuid list
     * @param queuedPlayerUUIDs a list of queued players
     */
    public void setQueuedPlayerUUIDs(List<UUID> queuedPlayerUUIDs) {
        this.queuedPlayerUUIDs = queuedPlayerUUIDs;
    }

    /**
     * get the size of the current duel queue
     * @return the size of the current duel queue
     */
    public int getQueuedPlayersSize()
    {
        return this.queuedPlayerUUIDs.size();
    }
}
