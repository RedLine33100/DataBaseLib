package fr.redline.dblp.dbv2.mysql.inter;

import fr.redline.dblp.dbv2.mysql.Location;

import java.util.Collection;

public interface SubCommand {
    String getCmdString();
    boolean acceptLocation(Location location);
    boolean needComa(Location location);
    boolean needAppendValue();
    void addedAt(Location location);
    Collection<Object> getValueToAppend();
    Collection<Location> getAvailableLocation();
}
