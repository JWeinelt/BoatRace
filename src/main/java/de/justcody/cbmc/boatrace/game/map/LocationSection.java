package de.justcody.cbmc.boatrace.game.map;

import de.justcody.cbmc.boatrace.util.locations.LocUtil;
import de.justcody.cbmc.boatrace.util.locations.LocationWrapper;
import lombok.Getter;
import org.bukkit.Location;

@Getter
public class LocationSection {
    private final LocationWrapper l1;
    private final LocationWrapper l2;

    public LocationSection(Location l1, Location l2) {
        this.l1 = LocUtil.fromBukkit(l1);
        this.l2 = LocUtil.fromBukkit(l2);
    }

    public LocationSection(LocationWrapper l1, LocationWrapper l2) {
        this.l1 = l1;
        this.l2 = l2;
    }
}
