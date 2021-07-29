package me.sknz.api.paladins.paladins.models.champions;

import java.util.Arrays;

public class ChampionSkin {

    public final String skinName;
    public final String skinNameEnglish;
    public final int skinId1;
    public final int skinId2;

    public final Rarity rarity;

    public ChampionSkin(String skinName, String skinNameEnglish, int skinId1, int skinId2, Rarity rarity) {
        this.skinName = skinName;
        this.skinNameEnglish = skinNameEnglish;
        this.skinId1 = skinId1;
        this.skinId2 = skinId2;
        this.rarity = rarity;
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
