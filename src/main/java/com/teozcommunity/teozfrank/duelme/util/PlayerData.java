package com.teozcommunity.teozfrank.duelme.util;

import org.apache.logging.log4j.core.pattern.UUIDPatternConverter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by frank on 04/06/2014.
 */
public class PlayerData {

    private UUID playerUUID;
    private ItemStack[] armour;
    private ItemStack[] inventory;
    private Location locaton;
    private Float saturation;
    private int foodLevel;
    private int expLevel;
    private double health;

    public PlayerData(UUID playerUUIDIn, ItemStack[] armourIn, ItemStack[] inventoryIn, Location locationIn,
                      Float saturationIn, int foodLevelIn, int expLevelIn, double healthIn) {
     this.playerUUID = playerUUIDIn;
     this.armour = armourIn;
     this.inventory = inventoryIn;
     this.locaton = locationIn;
     this.saturation = saturationIn;
     this.foodLevel = foodLevelIn;
     this.expLevel = expLevelIn;
     this.health = healthIn;
    }

    public UUID getUUID() {
        return playerUUID;
    }

    public void setUUID(UUID playerUUIDIn) {
        this.playerUUID = playerUUIDIn;
    }

    public ItemStack[] getArmour() {
        return armour;
    }

    public void setArmour(ItemStack[] armour) {
        this.armour = armour;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public Location getLocaton() {
        return locaton;
    }

    public void setLocaton(Location locaton) {
        this.locaton = locaton;
    }

    public Float getSaturation() {
        return saturation;
    }

    public void setSaturation(Float saturation) {
        this.saturation = saturation;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public void setFoodLevel(int hunger) {
        this.foodLevel = foodLevel;
    }

    public int getEXPLevel() {
        return expLevel;
    }

    public void setEXPLevel(int expIn) {
        this.expLevel = expIn;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }
}
