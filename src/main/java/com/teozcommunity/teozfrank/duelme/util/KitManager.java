package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 26/03/2016.
 */
public class KitManager {

    private DuelMe plugin;
    private List<Kit> kits;

    public KitManager(DuelMe plugin) {
        this.plugin = plugin;
        this.kits = new ArrayList<Kit>();
    }

    /**
     * add a kit to the list of kits
     * @param kit the kit object
     */
    public void addKit(Kit kit) {
        this.kits.add(kit);
    }


    /**
     * remove a kit from the list of kits
     * @param kit the kit object to remove
     */
    public void removeKit(Kit kit) {
        this.kits.remove(kit);
    }

    /**
     * get a list of the available kits
     * @return a list of kits
     */
    public List<Kit> getKits() {
        return this.kits;
    }

    /**
     * get a kit by its name
     * @param name the name of the kit to retrieve
     * @return the kit object, null if the kit does not exist
     */
    public Kit getKit(String name) {
        for(Kit kit: getKits()) {
            if(kit.getName().equals(name)) {
                return kit;
            }
        }
        return null;
    }


}
