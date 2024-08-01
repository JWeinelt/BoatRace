package de.justcody.cbmc.boatrace.game.map;

import de.justcody.cbmc.boatrace.util.locations.LocationWrapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CameraIntroPath {
    private LocationWrapper startPos;
    private LocationWrapper enPos;

    public CameraIntroPath(LocationWrapper startPos, LocationWrapper enPos) {
        this.startPos = startPos;
        this.enPos = enPos;
    }

    public CameraIntroPath() {

    }

}
