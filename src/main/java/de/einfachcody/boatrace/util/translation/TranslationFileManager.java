package de.einfachcody.boatrace.util.translation;

import de.einfachcody.boatrace.BoatRace;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class TranslationFileManager {
    public static HashMap<String, YamlConfiguration> configurations = new HashMap<>();

    public static void loadTranslations() {
        BoatRace.getPlugin().getLogger().info("Loading translations from folder...");
        File folder = new File(BoatRace.getPlugin().getDataFolder().getAbsolutePath() + "translation");
        File[] files = folder.listFiles();

        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(".yml")) {
                BoatRace.getPlugin().getLogger().info("Found translation file for language" +
                    " " + f.getName().replace(".yml", "") + ". Loading...");

                configurations.put(f.getName().replace(".yml", "").toLowerCase(),
                        YamlConfiguration.loadConfiguration(f));
            }
        }
        BoatRace.getPlugin().getLogger().info("Translations loaded!");
    }

    public static void createTranslationFiles() {

    }
}
