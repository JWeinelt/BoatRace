package de.justcody.cbmc.boatrace.game.map;

import de.justcody.cbmc.boatrace.game.GameType;
import de.justcody.cbmc.boatrace.util.locations.LocUtil;
import de.justcody.cbmc.boatrace.util.locations.LocationWrapper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Map {
    private String music;
    private String mapName;
    private String cup;
    private GameType gameType;
    private final List<LocationWrapper> startingPlaces = new ArrayList<>();
    private final List<LocationWrapper> itemBoxes = new ArrayList<>();
    private LocationSection startLine;
    private final List<LocationSection> checkpoints = new ArrayList<>();
    private String builder = "CodeBlocksMC";

    private CameraIntroPath cameraPath1;
    private CameraIntroPath cameraPath2;
    private CameraIntroPath cameraPath3;

    public void addItemBox(Location l) {
        itemBoxes.add(LocUtil.fromBukkit(l));
    }

    public void addCheckpoint(LocationSection locSel) {
        checkpoints.add(locSel);
    }
    public void addStartingPlace(LocationWrapper w) {
        startingPlaces.add(w);
    }


}