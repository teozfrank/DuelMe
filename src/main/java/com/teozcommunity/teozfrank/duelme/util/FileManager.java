package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
    private File locationsFile = null;


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
            plugin.sendConsoleMessage.severe("Error saving rewards config!");
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
}
