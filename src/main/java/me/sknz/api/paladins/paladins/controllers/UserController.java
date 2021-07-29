package me.sknz.api.paladins.paladins.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("paladins-users-controller")
@RequestMapping("api/paladins/users")
public class UserController {

    @GetMapping(value = "/{user}")
    public ResponseEntity<String> getUser(@PathVariable(name = "user") String user,
                                          @RequestParam(value = "platform", required = false, defaultValue = "pc") String platform){
        return null;
    }

    @GetMapping(value = "/{user}/champions")
    public ResponseEntity<String> getUserChampions(@PathVariable(name = "user") String user,
                                                   @RequestParam(value = "platform", required = false, defaultValue = "pc") String platform,
                                                   @RequestParam(value = "queue", required = false) String queue) {
        return null;
    }

    @GetMapping(value = "/{user}/loadouts")
    public ResponseEntity<String> getUserLoadouts(@PathVariable(name = "user") String user,
                                                   @RequestParam(value = "platform", required = false, defaultValue = "pc") String platform) {
        return null;
    }

    @GetMapping(value = "/{user}/matches")
    public ResponseEntity<String> getUserMatchHistory(@PathVariable(name = "user") String user,
                                                      @RequestParam(value = "platform", required = false, defaultValue = "pc") String platform,
                                                      @RequestParam(value = "queue", required = false, defaultValue = "siege") String queue) {
        return null;
    }
}
