package me.sknz.api.paladins.paladins.models.champions;

import java.util.List;

public class ChampionCards extends ChampionShort {

    public final List<ChampionCard> cards;
    public ChampionCards(List<ChampionCard> cards, int id, String name, String icon, Champion.Role role) {
        super(id, name, icon, role);
        this.cards = cards;
    }
}
