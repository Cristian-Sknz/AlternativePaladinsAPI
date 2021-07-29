package me.sknz.api.paladins.paladins.models.champions;

import java.util.List;

public class Champion extends ChampionShort {

    public final String title;
    public final String lore;
    public final int health;
    public final int speed;

    public final boolean latestChampion;
    public final boolean freeRotation;
    public final boolean freeWeeklyRotation;

    public final List<ChampionAbility> abilities;

    public enum Role {
        Damage, Flank, Tank, Support;
    }

    public Champion(int id, String name, String icon, Role role, String title, String lore, int health, int speed, boolean latestChampion, boolean freeRotation, boolean freeWeeklyRotation, List<ChampionAbility> abilities) {
        super(id, name, icon, role);
        this.title = title;
        this.lore = lore;
        this.health = health;
        this.speed = speed;
        this.latestChampion = latestChampion;
        this.freeRotation = freeRotation;
        this.freeWeeklyRotation = freeWeeklyRotation;
        this.abilities = abilities;
    }
}
