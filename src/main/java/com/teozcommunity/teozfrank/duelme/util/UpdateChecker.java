package com.teozcommunity.teozfrank.duelme.util;


/**
 * Created with IntelliJ IDEA.
 * User: Frank
 * Date: 17/08/13
 * Time: 23:42
 * To change this template use File | Settings | File Templates.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * This class is a barebones example of how to use the BukkitDev ServerMods API to check for file updates.
 * <br>
 * See the README file for further information of use.
 */
public class UpdateChecker {

    private DuelMe plugin;

    // The project's unique ID
    private final int projectID;


    // Keys for extracting file information from JSON response
    private static final String API_NAME_VALUE = "name";
    private static final String API_LINK_VALUE = "downloadUrl";
    private static final String API_RELEASE_TYPE_VALUE = "releaseType";
    private static final String API_FILE_NAME_VALUE = "fileName";
    private static final String API_GAME_VERSION_VALUE = "gameVersion";

    // Static information for querying the API
    private static final String API_QUERY = "/servermods/files?projectIds=";
    private static final String API_HOST = "https://api.curseforge.com";
    public boolean updateAvailable;

    /**
     * Check for updates anonymously (keyless)
     *
     * @param projectID The BukkitDev Project ID, found in the "Facts" panel on the right-side of your project page.
     */
    public UpdateChecker(DuelMe plugin, int projectID) {

        this.plugin = plugin;
        this.projectID = projectID;
        this.updateAvailable = false;
        query();
    }


    /**
     * Query the API to find the latest approved file's details.
     */
    public void query() {
        URL url = null;

        try {
            // Create the URL to query using the project's ID
            url = new URL(API_HOST + API_QUERY + projectID);
        } catch (MalformedURLException e) {
            // There was an error creating the URL

            e.printStackTrace();
            return;
        }

        try {
            // Open a connection and query the project
            URLConnection conn = url.openConnection();


            // Add the user-agent to identify the program
            conn.addRequestProperty("User-Agent", "ServerModsAPI-Example (by Gravity)");

            // Read the response of the query
            // The response will be in a JSON format, so only reading one line is necessary.
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();

            // Parse the array of files from the query's response
            JSONArray array = (JSONArray) JSONValue.parse(response);

            if (array.size() > 0) {
                // Get the newest file's details
                JSONObject latest = (JSONObject) array.get(array.size() - 1);

                // Get the version's title
                String versionName = (String) latest.get(API_NAME_VALUE);

                // Get the version's link
                String versionLink = (String) latest.get(API_LINK_VALUE);

                // Get the version's release type
                String versionType = (String) latest.get(API_RELEASE_TYPE_VALUE);

                // Get the version's file name
                String versionFileName = (String) latest.get(API_FILE_NAME_VALUE);

                // Get the version's game version
                String versionGameVersion = (String) latest.get(API_GAME_VERSION_VALUE);


                versionName = versionName.replaceAll("[a-zA-Z]", "");
                versionName = versionName.replaceAll(" ","");

                if(!versionName.equals(plugin.getDescription().getVersion())){
                    this.updateAvailable = true;
                    plugin.getConsoleMessageSender().info("There is a new update available! download it on bukkit dev "+
                            ChatColor.YELLOW+"http://dev.bukkit.org/bukkit-plugins/duelme/");
                }
                else {
                    this.updateAvailable = false;
                    plugin.getConsoleMessageSender().info("plugin is up to date!");
                }

            } else {
                System.out.println("There are no files for this project");
            }
        } catch (IOException e) {
            // There was an error reading the query

            e.printStackTrace();
            return;
        }
    }

    public boolean isUpdateAvailable(){
        return this.updateAvailable;
    }

}
