package me.sknz.api.paladins.authentication.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JWTAuthenticatedModel {

    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private long expires;

    public JWTAuthenticatedModel() {
    }

    public JWTAuthenticatedModel(String accessToken, long expires) {
        this.tokenType = "Bearer";
        this.accessToken = accessToken;
        this.expires = expires;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }
}
