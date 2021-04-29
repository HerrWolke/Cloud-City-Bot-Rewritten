package de.mrcloud.utils;

import java.util.LinkedHashMap;

public class Languages {
    LinkedHashMap<String,String> german;
    LinkedHashMap<String,String> english;

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
}
