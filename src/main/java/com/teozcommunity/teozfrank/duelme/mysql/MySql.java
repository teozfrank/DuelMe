package com.teozcommunity.teozfrank.duelme.mysql;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.FileManager;
import com.teozcommunity.teozfrank.duelme.util.SendConsoleMessage;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;

/**
 * Created by frank on 11/02/14.
 */
public class MySql {

    private DuelMe plugin;

    public MySql(DuelMe plugin) {
        this.plugin = plugin;
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
     * @param player the players name
     * @param fieldNameIn the enum field name
     */
    public void addPlayerKillDeath(String player, FieldName fieldNameIn) {
        String fieldName = null;

        if(fieldNameIn == FieldName.DEATHS){
            fieldName = "DEATHS";
        } else if(fieldNameIn == FieldName.KILLS) {
            fieldName = "KILLS";
        } else {
            //do nothing
        }
        Connection connection = this.getConnection();
        String query = "SELECT * from STATS WHERE PLAYER='" + player + "'";
        int p = 0;

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                p++;
            }

            if (p == 0) {
                this.addNewPlayerKillDeath(player,fieldName);
            } else if (p == 1) {
                this.addExistingPlayerKillDeath(player, fieldName);
            } else {
                SendConsoleMessage.severe("more than one record was found for a player!! failed to add "
                        + fieldName + " record!");
            }
            result.close();
            connection.close();
        } catch (SQLException e) {
            SendConsoleMessage.severe("error adding all time player vote!!" + e);
        }
    }

    /**
     * add a player kill or death depending on the
     * field name passed in
     * @param player the players name
     * @param fieldName the string field name
     */
    private void addExistingPlayerKillDeath(String player,String fieldName) {
        Connection connection = this.getConnection();
        String query = "SELECT * FROM STATS WHERE PLAYER ='" + player + "'";
        int killDeathCount = 0;
        int rows = 0;

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                killDeathCount = result.getInt(fieldName);
                rows++;
            }
            result.close();
            statement.close();
            if (!(rows > 1)) {
                int newKillDeathValue = killDeathCount + 1;
                String sql = "UPDATE STATS SET " + fieldName + " ='" + newKillDeathValue + " WHERE `PLAYER`='" + player + "'";
                Statement statement2 = this.getConnection().createStatement();
                statement2.executeUpdate(sql);
                statement2.close();
            } else {
                SendConsoleMessage.severe("Duplicate player names please check database!");
            }
        } catch (SQLException e) {
            SendConsoleMessage.severe("SQL Error!" + e);
        }

    }

    /**
     * adds a new player kill or death record to the database
     * depending on the field name passed in
     * @param player the players name
     * @param fieldName the string field name
     */
    private void addNewPlayerKillDeath(String player, String fieldName) {

        String sql = "UPDATE STATS SET " + fieldName + " ='1' WHERE `PLAYER`='" + player + "'";
        try {
            Statement statement = this.getConnection().createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            SendConsoleMessage.severe("ERROR inserting new player " + fieldName + " record!" + e);
        }
    }

}
