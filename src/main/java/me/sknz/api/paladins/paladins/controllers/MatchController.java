package me.sknz.api.paladins.paladins.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("paladins-match-controller")
@RequestMapping("api/paladins/matches")
public class MatchController {

    @GetMapping(value = "/{matchId}")
    public ResponseEntity<String> getMatchDetails(@PathVariable("matchId") long matchId) {
        return null;
    }

    @GetMapping(value = "/live/{matchId}")
    public ResponseEntity<String> getLiveMatchDetails(@PathVariable("matchId") long matchId) {
        return null;
    }

    @GetMapping(value = "/top")
    public ResponseEntity<String> getTopMatches() {
        return null;
    }
}
