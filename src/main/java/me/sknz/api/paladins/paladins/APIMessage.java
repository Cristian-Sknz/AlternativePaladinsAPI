package me.sknz.api.paladins.paladins;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public enum APIMessage {

    Approved(HttpStatus.ACCEPTED, "Approved"),
    DailyLimitExceeded(HttpStatus.TOO_MANY_REQUESTS,"DailyLimit", "Daily request limit reached"),
    SessionLimitExceeded(HttpStatus.TOO_MANY_REQUESTS, "Exceeded daily session cap."),
    IncorrectTimeStamp(HttpStatus.BAD_REQUEST, "Error while comparing Server and Client timestamp", "Exception - Timestamp"),
    InvalidSignature(HttpStatus.BAD_REQUEST, "Exception while validating developer access.Invalid signature.", "Invalid signature"),
    InvalidDeveloperId(HttpStatus.BAD_REQUEST, "Failed to validate DeveloperId", "Unauthorized Developer Id"),
    InvalidDateFormat(HttpStatus.BAD_REQUEST, "Invalid date format", "Year, Month, and Day parameters describe an un-representable DateTime."),
    InvalidSessionId(HttpStatus.BAD_REQUEST, "Invalid session id."),
    ActiveSessionLimit(HttpStatus.TOO_MANY_REQUESTS, "Maximum number of active sessions reached.");

    private final String[] messages;
    private final HttpStatus possibleStatus;

    APIMessage(HttpStatus possibleStatus, String... message) {
        this.messages = message;
        this.possibleStatus = possibleStatus;
    }

    public String[] getMessages(){
        return messages;
    }

    public static APIMessage getAPIMessageByString(String message) {
        if (message == null){
            return null;
        }
        Optional<APIMessage> optional = Stream.of (APIMessage.values()).filter(api -> Arrays.stream(api.messages)
                .anyMatch(msg -> message.toLowerCase().contains(msg.toLowerCase())))
                .findFirst();

        return optional.orElse(null);
    }

    public static boolean isValidResponse(String message){
        APIMessage msg = getAPIMessageByString(message);
        return msg == null || msg == Approved;
    }

    public HttpStatus getPossibleStatus() {
        return possibleStatus;
    }
}
