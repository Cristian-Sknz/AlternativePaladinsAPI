package me.sknz.api.paladins.paladins.models.champions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChampionCards extends ChampionShort {

    public final List<ChampionCard> cards;

    public ChampionCards(ChampionShort championShort, ArrayNode array) {
        super(championShort);
        this.cards = new ArrayList<>();
        for (JsonNode jsonNode : array) {
            cards.add(new ChampionCard(jsonNode));
        }
    }

    public ChampionCards(ChampionShort championShort, List<ChampionCard> cards) {
        super(championShort);
        this.cards = cards;
    }

    public static List<ChampionCards> from(Champions champions, ArrayNode allChampionSkins) {
        List<ChampionCard> map = new ArrayList<>();
        for (JsonNode node : allChampionSkins){
            map.add(new ChampionCard(node));
        }
        return champions.stream()
                .map(champion -> new ChampionCards(champion, map.stream()
                        .filter(card -> card.championId == champion.id).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
}
