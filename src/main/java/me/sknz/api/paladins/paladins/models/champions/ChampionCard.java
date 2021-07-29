package me.sknz.api.paladins.paladins.models.champions;

public class ChampionCard {

    public String cardName;
    public String cardNameEnglish;
    public int cardId1;
    public int cardId2;
    public String description;
    public String icon;
    public boolean isLegendary;
    public int rechargeSeconds;

    public ChampionCard(String cardName, String cardNameEnglish, int cardId1, int cardId2, String description, String icon, boolean isLegendary, int rechargeSeconds) {
        this.cardName = cardName;
        this.cardNameEnglish = cardNameEnglish;
        this.cardId1 = cardId1;
        this.cardId2 = cardId2;
        this.description = description;
        this.icon = icon;
        this.isLegendary = isLegendary;
        this.rechargeSeconds = rechargeSeconds;
    }
}
