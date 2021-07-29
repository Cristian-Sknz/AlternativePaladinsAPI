package me.sknz.api.paladins.paladins.controllers;

import me.sknz.api.paladins.paladins.models.champions.Champion;
import me.sknz.api.paladins.paladins.models.champions.ChampionCards;
import me.sknz.api.paladins.paladins.models.champions.ChampionSkins;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("paladins-champion-controller")
@RequestMapping("api/paladins/champions")
public class ChampionController {

    @GetMapping(value = "/")
    public ResponseEntity<List<Champion>> getChampions(){
        return null;
    }

    @GetMapping(value = "/{champion}")
    public ResponseEntity<Champion> getChampion(@PathVariable(name = "champion") String champion){
        return null;
    }

    @GetMapping(value = "/skins")
    public ResponseEntity<List<ChampionSkins>> getChampionsSkins(){
        return null;
    }

    @GetMapping(value = "/skins/{champion}")
    public ResponseEntity<ChampionSkins> getChampionSkins(@PathVariable(name = "champion") String champion){
        return null;
    }

    @GetMapping(value = "/cards/{champion}")
    public ResponseEntity<ChampionCards> getChampionCards(@PathVariable(name = "champion") String champion){
        return null;
    }
}
