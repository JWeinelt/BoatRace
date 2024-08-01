package de.einfachcody.boatrace.util.translation;


public class Translation {
    public String translate(String langCode, String key) {
        return TranslationFileManager.configurations.getOrDefault(langCode,
                TranslationFileManager.configurations.get("en")).getString(key);
    }
}
