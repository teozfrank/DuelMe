package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * Original Author: teozfrank
 * Date: 17/08/13
 * Time: 23:42
 * -----------------------------
 * Removing this header is in breach of the license agreement,
 * please do not remove, move or edit it in any way.
 * -----------------------------
 */
public class UpdateChecker {

    private DuelMe plugin;
    private URL fileFeed;
    private String version;
    private String version2;
    private String link;

    public UpdateChecker(DuelMe plugin, String url) {
        this.plugin = plugin;
        try {
            this.fileFeed = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * method to check if a plugin update is available
     * @return true if the plugin is up to date
     */
    public boolean updateAvailable() {
        try {
            InputStream input = this.fileFeed.openConnection().getInputStream();
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
            Node latest = document.getElementsByTagName("item").item(0);
            NodeList children = latest.getChildNodes();

            this.version2 = children.item(1).getTextContent().replaceAll("[a-zA-Z]", "");//trims letters
            this.version = version2.replaceAll(" ", "");//trims white space
            this.link = children.item(3).getTextContent();

            if (!plugin.getDescription().getVersion().equals(this.version)) {
                return true;
            }
        } catch (Exception e) {
            plugin.sendConsoleMessage.severe("error trying to check for updates!");
        }
        return false;
    }

    /**
     *
     * @return the latest version of the plugin
     */
    public String getVersion() {
        return this.version;
    }

    /**
     *
     * @return the latest download url of the plugin
     */
    public String getLink() {
        return this.link;
    }
}
