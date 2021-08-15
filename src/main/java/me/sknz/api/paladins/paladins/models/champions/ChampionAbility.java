package me.sknz.api.paladins.paladins.models.champions;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;

public class ChampionAbility {

    public final int id;
    public final String name;
    public final String icon;
    public final DamageType damageType;
    public final float cooldown;

    public enum DamageType {
        AoE, Direct, None;

        DamageType() {
        }

        public static DamageType getDamageType(String damage){
            return Arrays.stream(values()).filter(dmg -> dmg.name().equalsIgnoreCase(damage))
            .findFirst().orElse(None);
        }
    }

    public ChampionAbility(JsonNode ability) {
        this(ability.get("Id").asInt(),
                ability.get("Summary").asText(),
                ability.get("URL").asText(),
                DamageType.getDamageType(ability.get("damageType").asText()),
                ability.get("rechargeSeconds").asInt());
    }

    public ChampionAbility(int id, String name, String icon, DamageType damageType, float cooldown) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.damageType = damageType;
        this.cooldown = cooldown;
    }
}
