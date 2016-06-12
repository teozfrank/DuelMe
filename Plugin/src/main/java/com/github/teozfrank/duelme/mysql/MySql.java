package com.github.teozfrank.duelme.mysql;

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

import com.github.teozfrank.duelme.main.DuelMe;
import com.github.teozfrank.duelme.util.FileManager;
import com.github.teozfrank.duelme.util.SendConsoleMessage;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;
import java.util.UUID;

public class MySql {

    private DuelMe plugin;

    public MySql(DuelMe plugin) {
        this.plugin = plugin;
        FileManager fm = plugin.getFileManager();

        if(fm.isMySqlEnabled()) {
            SendConsoleMessage.info("MySql Enabled, connecting to database.");
            this.setupTables();
        }
    }

    /**
     * setup our connection
     * @return the connection object
     */
    public Connection getConnection() {
        try {
            FileConfiguration config = plugin.getConfig();
            String MySqlHost = config.getString("duelme.mysql.host");
            String MySqlPort = config.getString("duelme.mysql.port");
            String MySqlDatabase = config.getString("duelme.mysql.database");
            String MySqlUsername = config.getString("duelme.mysql.user");
            String MySqlPassword = config.getString("duelme.mysql.pass");

            return DriverManager.getConnection("jdbc:mysql://" + MySqlHost + ":" + MySqlPort + "/" + MySqlDatabase, MySqlUsername, MySqlPassword);
        } catch (SQLException e) {
            SendConsoleMessage.warning("MySql could not establish a connection!" + e);
        }
        return null;
    }

    /**
     * setup the table for the database
     * if they exist
     */
    public void setupTables() {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = this.getConnection();
            SendConsoleMessage.info("Successfully connected to MySql.");
            String query = "SHOW TABLES LIKE 'STATS'";
            PreparedStatement statement = connection.prepareStatement(query);
            int i;
            ResultSet result = statement.executeQuery();
            i = 0;
            while (result.next()) {
                i++;
            }
            result.close();
            statement.close();
            if (!(i > 0)) {
                SendConsoleMessage.info("Table STATS does not exist creating it for you!");
                String sql = "CREATE TABLE STATS "
                        + "(ID MEDIUMINT NOT NULL AUTO_INCREMENT,"
                        + " UUID VARCHAR(50), "
                        + " PLAYER VARCHAR(50), "
                        + " KILLS MEDIUMINT ,"
                        + " DEATHS MEDIUMINT,"
                        + " PRIMARY KEY ( ID ))";
                statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                statement.close();
            }
            connection.close();
        } catch (ClassNotFoundException ex) {
            SendConsoleMessage.warning("MySql driver wasn't found!");
        } catch (Exception e) {
            SendConsoleMessage.warning("MySql could not establish a connection: " + e);
        }
    }

    /**
     * method to add a kill or death depending on what field name
     * is passed in
     * @param playerUUID the players unique id
     * @param fieldNameIn the enum field name
     */
    public void addPlayerKillDeath(UUID playerUUID, String playerName, FieldName fieldNameIn) {
        String fieldName = null;

        if(fieldNameIn == FieldName.DEATH){
            fieldName = "DEATHS";
        } else if(fieldNameIn == FieldName.KILL) {
            fieldName = "KILLS";
        } else {
            //do nothing
        }
        Connection connection = this.getConnection();
        String query = "SELECT * from STATS WHERE UUID='" + playerUUID + "'";
        int p = 0;

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                p++;
            }

            if (p == 0) {
                this.addNewPlayerKillDeath(playerUUID, playerName, fieldName);
            } else if (p == 1) {
                this.addExistingPlayerKillDeath(playerUUID, playerName, fieldName);
            } else {
                SendConsoleMessage.severe("more than one record was found for a player!! failed to add "
                        + fieldName + " record!");
            }
            result.close();
            connection.close();
        } catch (SQLException e) {
            SendConsoleMessage.severe("error adding all time player " + fieldName + "\n" + e);
        }
    }

    /**
     * add a player kill or death depending on the
     * field name passed in
     * @param playerName the players name
     * @param fieldName the string field name
     */
    private void addExistingPlayerKillDeath(UUID playerUUID, String playerName,String fieldName) {
        Connection connection = this.getConnection();
        String query = "SELECT * FROM STATS WHERE UUID ='" + playerUUID + "'";
        int killDeathCount = 0;
        int rows = 0;
        String playerNameSQL = "";

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                killDeathCount = result.getInt(fieldName);
                playerNameSQL = result.getString("PLAYER");
                rows++;
            }
            result.close();
            statement.close();
            if (!(rows > 1)) {
                int newKillDeathValue = killDeathCount + 1;
                String sql = "UPDATE STATS SET " + fieldName + " ='" + newKillDeathValue + "' WHERE UUID='" + playerUUID + "'";
                Statement statement2 = this.getConnection().createStatement();
                statement2.executeUpdate(sql);
                statement2.close();

                if(!playerName.equals(playerNameSQL)) { //if the players name differs from the name returned from the database
                    String sql2 = "UPDATE STATS SET PLAYER ='" + playerName + "' WHERE UUID='" + playerUUID + "'";
                    Statement statement3 = this.getConnection().createStatement();
                    statement3.executeUpdate(sql2);//update the database with the new players name.
                    statement3.close();
                }
                connection.close();
            } else {
                SendConsoleMessage.severe("Duplicate player names please check database!");
            }
        } catch (SQLException e) {
            SendConsoleMessage.severe("SQL Error adding existing player kill/death !" + e);
        }

    }

    /**
     * adds a new player kill or death record to the database
     * depending on the field name passed in
     * @param playerName the players name
     * @param fieldName the string field name
     */
    private void addNewPlayerKillDeath(UUID playerUUID, String playerName, String fieldName) {
        String sql = null;
        if(fieldName.equals("KILLS")) {
            sql = "INSERT INTO STATS VALUES (null,'" + playerUUID  +"', '" + playerName + "','" + 1 + "','" + 0 + "')";
        } else if(fieldName.equals("DEATHS")) {
            sql = "INSERT INTO STATS VALUES (null,'" + playerUUID  +"', '" + playerName + "','" + 0 + "','" + 1 + "')";
        } else {

        }
        try {
            Statement statement = this.getConnection().createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            SendConsoleMessage.severe("ERROR inserting new player " + fieldName + " record!" + e);
        }
    }

}
