package de.einfachcody.boatrace.game.arena;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.einfachcody.boatrace.BoatRace;
import de.einfachcody.boatrace.util.LocationConverter;
import org.bukkit.entity.Boat;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;

public class ArenaManager {
    private Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public Arena toArena(ArenaWrapper w) {
        Arena a = new Arena();
        a.setAmountGoals(w.getAmountGoals());
        a.setGoal1(w.getGoal1());
        a.setGoal2(w.getGoal2());
        a.setGoal3(w.getGoal3());

        a.setRounds(w.getRounds());
        a.setDisplayName(w.getDisplayName());
        a.setStartPos1(LocationConverter.toBukkit(w.getStartPos1()));
        a.setStartPos2(LocationConverter.toBukkit(w.getStartPos2()));
        a.setStartPos3(LocationConverter.toBukkit(w.getStartPos3()));
        a.setStartPos4(LocationConverter.toBukkit(w.getStartPos4()));
        a.setStartPos5(LocationConverter.toBukkit(w.getStartPos5()));
        a.setStartPos6(LocationConverter.toBukkit(w.getStartPos6()));

        a.setItemBoxes(LocationConverter.toBukkitList(w.getItemBoxes()));

        a.setCheckpoints(w.getCheckpoints());

        return a;
    }

    public void saveArena(ArenaWrapper wrapper) {
        File file = new File(BoatRace.getPlugin().getDataFolder() + "arenas/" + wrapper.getDisplayName() + ".json");
        String text = GSON.toJson(wrapper);
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath())));
            bw.write(text);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            BoatRace.getPlugin().getLogger().log(Level.SEVERE, "Error while Saving Arena File", e);
        }
    }

    public Arena loadArena(String name) {
        File file = new File(BoatRace.getPlugin().getDataFolder() + "arenas/" + name + ".json");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            Arena t = GSON.fromJson(br, new TypeToken<Arena>() {}.getType());

            br.close();
            return t;
        } catch (Exception e) {
            BoatRace.getPlugin().getLogger().log(Level.SEVERE, "Error while Loading Arena file File for arena " + name, e);
            return null;
        }
    }
}
