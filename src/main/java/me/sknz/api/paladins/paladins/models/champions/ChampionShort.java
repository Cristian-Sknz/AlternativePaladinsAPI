package me.sknz.api.paladins.paladins.models.champions;

public class ChampionShort {

    public final int id;
    public final String name;
    public final String icon;
    public final Champion.Role role;

    public ChampionShort(int id, String name, String icon, Champion.Role role) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.role = role;
    }
}
