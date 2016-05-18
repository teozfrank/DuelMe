package com.teozcommunity.teozfrank.duelme.util;


import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Frank on 26/03/2016.
 */
public class Kit {

    private String name;
    private List<ItemStack> kitItems;

    public Kit() {

    }

    public Kit(String name, List<ItemStack> kitItems) {
        this.name = name;
        this.kitItems = kitItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemStack> getKitItems() {
        return kitItems;
    }

    public void setKitItems(List<ItemStack> kitItems) {
        this.kitItems = kitItems;
    }
}
