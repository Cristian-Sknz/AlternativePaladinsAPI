package me.sknz.api.paladins.paladins.models.champions;

import java.util.List;

public class ChampionSkins extends ChampionShort {

    public final List<ChampionSkin> skins;

    public ChampionSkins(List<ChampionSkin> skins, int id, String name, String icon, Champion.Role role) {
        super(id, name, icon, role);
        this.skins = skins;
    }
}
