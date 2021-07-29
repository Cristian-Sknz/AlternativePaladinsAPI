package me.sknz.api.paladins.exception;

import me.sknz.api.paladins.paladins.APIMessage;

public class PaladinsAPIException extends RuntimeException {

    public PaladinsAPIException(String message) {
        super(message);
    }

    public PaladinsAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public APIMessage getAPIMessage(){
        return APIMessage.getAPIMessageByString(this.getMessage());
    }
}
