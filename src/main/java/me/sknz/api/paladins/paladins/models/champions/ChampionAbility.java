package me.sknz.api.paladins.paladins.models.champions;

public class ChampionAbility {

    public final int id;
    public final String name;
    public final String icon;
    public final DamageType damageType;
    public final float cooldown;

    public enum DamageType {
        AoE, Direct;
    }

    public ChampionAbility(int id, String name, String icon, DamageType damageType, float cooldown) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.damageType = damageType;
        this.cooldown = cooldown;
    }
}
