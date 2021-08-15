package me.sknz.api.paladins.paladins.models;

import java.util.Arrays;

public enum APILanguage {

    English("English", 1),
    German("German", 2),
    French("French", 3),
    Chinese("Chinese", 5),
    Spanish("Spanish", 7),
    SpanishLA("SpanishLA", 9),
    Portuguese("Portuguese", 10),
    Russian("Russian", 11),
    Polish("Polish", 12),
    Turkish("Turkish", 13);

    private final String name;
    private final int value;

    APILanguage(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static APILanguage getByValue(long value){
        return Arrays.stream(values()).filter(lang -> lang.getValue() == value).findFirst().orElse(English);
    }

    public static APILanguage getByValue(String value){
        if (value.matches("\\d+"))
            return getByValue(Integer.parseInt(value));

        return Arrays.stream(values()).filter(lang -> lang.getName().equalsIgnoreCase(value)).findFirst().orElse(English);
    }
}
