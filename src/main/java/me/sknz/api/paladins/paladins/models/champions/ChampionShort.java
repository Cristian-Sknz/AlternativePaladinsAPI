package me.sknz.api.paladins.paladins.models.champions;

import com.fasterxml.jackson.databind.JsonNode;

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

    public <T extends ChampionShort> ChampionShort(ChampionShort championShort) {
        this(championShort.id, championShort.name, championShort.icon, championShort.role);
    }

    public ChampionShort(JsonNode node) {
        this.id = node.get("id").asInt();
        this.name = node.get("Name").asText();
        this.icon = node.get("ChampionIcon_URL").asText();
        this.role = Champion.Role.getRole(node.get("Roles").asText().split(" ")[1]);
                   // In Json the API returns "Paladins Damage", so you need to use split
    }

}
