package util;

import org.bukkit.Location;

public class WorldEditSelection {

    private Location pos1;
    private Location pos2;

    private boolean success;

    public WorldEditSelection() {

    }

    public WorldEditSelection(Location pos1, Location pos2, boolean success) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.success = success;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        String returnString = "";
        if(pos1 == null || pos2 == null) {
            returnString = "Selection is null or incomplete";
        } else {
            returnString = "WorldEditSelection{" +

                    "pos1W=" + pos1.getWorld().getName() +
                    "pos1X=" + pos1.getBlockX() +
                    "pos1Y=" + pos1.getBlockY() +
                    "pos1Z=" + pos1.getBlockZ() +"\n" +
                    "pos2W=" + pos2.getWorld().getName() +
                    "pos2X=" + pos2.getBlockX() +
                    "pos2Y=" + pos2.getBlockY() +
                    "pos2Z=" + pos2.getBlockZ() +"\n" +
                    "success=" + success +
                    '}';
        }
        return returnString;
    }
}
