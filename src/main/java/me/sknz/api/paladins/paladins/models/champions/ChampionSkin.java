package me.sknz.api.paladins.paladins.models.champions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;

public class ChampionSkin {

    @JsonIgnore
    public final int championId;

    public final String skinName;
    public final String skinNameEnglish;
    public final int skinId1;
    public final int skinId2;

    public final Rarity rarity;

    public ChampionSkin(JsonNode node) {
        this.championId = node.get("champion_id").asInt();
        this.skinName = node.get("skin_name").asText();
        this.skinNameEnglish = node.get("skin_name_english").asText();
        this.skinId1 = node.get("skin_id1").asInt();
        this.skinId2 = node.get("skin_id2").asInt();
        this.rarity = Rarity.getRarityByName(node.get("rarity").asText());
    }

    public enum Rarity {

        Common(0), Uncommom(1), Rare(2), Epic(3), Legendary(4), Limited(5), Ilimited(6);

        private final int id;

        Rarity(int id) {
            this.id = id;
        }

        public static Rarity getRarityByName(String name) {
            return Arrays.stream(values())
                    .filter(rarity -> rarity.name().equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(null);
        }

        public int getId() {
            return id;
        }
    }
}
