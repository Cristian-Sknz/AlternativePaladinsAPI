package me.sknz.api.paladins.paladins.models.champions;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Arrays;
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

        public static Role getRole(String name){
            return Arrays.stream(values())
                    .filter(role -> role.name().charAt(0) == name.charAt(0))
                    .findFirst()
                    .orElse(null);
        }

    }

    public Champion(JsonNode node) {
        super(node);
        this.title = node.get("Title").asText();
        this.lore = node.get("Lore").asText();
        this.health = node.get("Health").asInt();
        this.speed = node.get("Speed").asInt();
        this.latestChampion = node.get("latestChampion").asText().equals("y");
        this.freeRotation = !node.get("OnFreeRotation").asText().isEmpty();
        this.freeWeeklyRotation = !node.get("OnFreeWeeklyRotation").asText().isEmpty();
        this.abilities = new ArrayList<>();
        for (int i = 1; i <= 5; i++){
            abilities.add(new ChampionAbility(node.get(String.format("Ability_%s", i))));
        }
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
