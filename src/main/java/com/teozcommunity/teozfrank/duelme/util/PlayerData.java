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

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerData {


    private ItemStack[] armour;
    private ItemStack[] inventory;
    private Location locaton;
    private Float saturation;
    private int foodLevel;
    private int expLevel;
    private double health;

    public PlayerData(ItemStack[] armourIn, ItemStack[] inventoryIn, Location locationIn,
                      Float saturationIn, int foodLevelIn, int expLevelIn, double healthIn) {
     this.armour = armourIn;
     this.inventory = inventoryIn;
     this.locaton = locationIn;
     this.saturation = saturationIn;
     this.foodLevel = foodLevelIn;
     this.expLevel = expLevelIn;
     this.health = healthIn;
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
