package me.sknz.api.paladins.paladins.models.champions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChampionSkins extends ChampionShort {

    public final List<ChampionSkin> skins;

    public ChampionSkins(ChampionShort championShort, ArrayNode array) {
        super(championShort);
        this.skins = new ArrayList<>();
        for (JsonNode node : array) {
            this.skins.add(new ChampionSkin(node));
        }
    }

    public ChampionSkins(ChampionShort championShort, List<ChampionSkin> skins) {
        super(championShort);
        this.skins = skins;
    }

    public static List<ChampionSkins> from(Champions champions, ArrayNode allChampionSkins) {
        List<ChampionSkin> map = new ArrayList<>();
        for (JsonNode node : allChampionSkins){
            map.add(new ChampionSkin(node));
        }
        return champions.stream()
                .map(champion -> new ChampionSkins(champion, map.stream()
                        .filter(skin -> skin.championId == champion.id).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
}
