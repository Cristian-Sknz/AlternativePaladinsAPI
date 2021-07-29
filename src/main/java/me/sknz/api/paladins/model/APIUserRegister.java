package me.sknz.api.paladins.model;

import me.sknz.api.paladins.authentication.model.APIUserModel;

import javax.validation.constraints.*;

public class APIUserRegister {

    @Min(value = 4, message = "validation.devid.length")
    private int devId;

    @NotBlank(message = "validation.authkey.invalid")
    @Size(min = 32, max = 40, message = "validation.authkey.invalid")
    private String authKey;

    @NotBlank(message = "validation.password.empty")
    @Size(min = 8, message = "validation.password.length")
    private String password;

    public APIUserRegister() {
    }

    public APIUserRegister(String email, String password, int devId, String authKey) {
        this.password = password;
        this.devId = devId;
        this.authKey = authKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDevId() {
        return devId;
    }

    public void setDevId(int devId) {
        this.devId = devId;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public APIUserModel toUser(){
        return new APIUserModel(this);
    }
}
