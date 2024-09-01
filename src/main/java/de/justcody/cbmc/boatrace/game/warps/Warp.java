package de.justcody.cbmc.boatrace.game.warps;

import de.justcody.cbmc.boatrace.game.map.LocationSection;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.Vector;

@Getter
@Setter
public class Warp {
    private LocationSection triggerSection;

    private Vector velocity;
    private LocationSection endSection;

    private String id;

    public Warp(LocationSection triggerSection, Vector velocity, LocationSection endSection, String id) {
        this.triggerSection = triggerSection;
        this.velocity = velocity;
        this.endSection = endSection;
        this.id = id;
    }
}
