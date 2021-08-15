package me.sknz.api.paladins.paladins.models.champions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

public class ChampionCard {

    @JsonIgnore
    public final int championId;

    public final String cardName;
    public final String cardNameEnglish;
    public final int cardId1;
    public final int cardId2;
    public final float cardValue;
    public final String description;
    public final String icon;
    public final boolean isLegendary;
    public final int rechargeSeconds;

    public ChampionCard(JsonNode node) {
        this.championId = node.get("champion_id").asInt();
        this.cardName = node.get("card_name").asText();
        this.cardNameEnglish = node.get("card_name_english").asText();
        this.cardId1 = node.get("card_id1").asInt();
        this.cardId2 = node.get("card_id2").asInt();
        this.cardValue = cardValueMultiplicator(node.get("card_description").asText());
        String multiplyString = String.valueOf(cardValue).replace(".0", "");
        this.description = node.get("card_description").asText()
                .replace("{scale=" + multiplyString + "|" + multiplyString + "}", "%s")
                .replace("{scale=" + multiplyString + "|" + multiplyString + ")", "%s");
        this.icon = node.get("championCard_URL").asText();
        this.isLegendary = ChampionSkin.Rarity.getRarityByName(node.get("rarity").asText()) == ChampionSkin.Rarity.Legendary;
        this.rechargeSeconds = node.get("recharge_seconds").asInt();
    }

    private static float cardValueMultiplicator(String cardDescription) {
        int chaveinicial = 0;
        int chavefinal = 0;
        int i = 0;
        for (char ch : cardDescription.toCharArray()) {
            if (ch == '{') {
                chaveinicial = i;
            }
            if (ch == '}' || ch == ')') {
                chavefinal = i + 1;
            }
            i++;
        }

        String desc = cardDescription.substring(chaveinicial, chavefinal);
        if (desc.length() == 0 || desc.length() == 1) {
            return 0;
        }
        String splited = desc.split("=")[1];
        int middle = 0;
        for (char ch : splited.toCharArray()) {
            if (ch == '|') {
                break;
            }
            middle++;
        }
        return Float.parseFloat(splited.substring(0, middle));
    }
}
