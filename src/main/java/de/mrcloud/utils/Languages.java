package de.mrcloud.utils;

import java.util.LinkedHashMap;

public class Languages {
    LinkedHashMap<String, String> german;
    LinkedHashMap<String, String> english;

    public String getTranslation(LanguageType languageType, String translation) {
        if (languageType.equals(LanguageType.GERMAN)) {
            return german.get(translation);
        } else {
            return english.get(translation);
        }
    }

    public String getTranslation(String language, String translation) {
        if (language.equalsIgnoreCase("ger")) {
            return german.get(translation);
        } else {
            return english.get(translation);
        }
    }

    public LinkedHashMap<String, String> getGerman() {
        return german;
    }

    public void setGerman(LinkedHashMap<String, String> german) {
        this.german = german;
    }

    public LinkedHashMap<String, String> getEnglish() {
        return english;
    }

    public void setEnglish(LinkedHashMap<String, String> english) {
        this.english = english;
    }

    public enum LanguageType {
        GERMAN, ENGLISH
    }
}
