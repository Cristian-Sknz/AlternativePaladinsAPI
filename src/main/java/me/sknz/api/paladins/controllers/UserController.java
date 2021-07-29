package me.sknz.api.paladins.controllers;

import me.sknz.api.paladins.exception.ExistingUserException;
import me.sknz.api.paladins.model.APIUserRegister;
import me.sknz.api.paladins.model.PaladinsDeveloper;
import me.sknz.api.paladins.respository.APIUserRepository;
import me.sknz.api.paladins.authentication.model.APIUserModel;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("api")
@Deprecated
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private APIUserRepository repository;

    @PostMapping(value = "/register")
    public ResponseEntity<APIUserModel> createUser(@Valid @RequestBody APIUserRegister register){
        if (repository.existsById(register.getDevId())){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "", new ExistingUserException("Este usuário já existe!"));
        }
        register.setPassword(passwordEncoder.encode(register.getPassword()));
        APIUserModel user = register.toUser();
        repository.save(user);
        user.setDeveloper(new PaladinsDeveloper(user, register.getAuthKey()));
        return ResponseEntity.ok(repository.save(user));
    }
}
