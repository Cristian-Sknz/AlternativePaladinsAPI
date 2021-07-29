package me.sknz.api.paladins.paladins;

import org.springframework.lang.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.SimpleTimeZone;
import java.util.stream.Collectors;

public class PaladinsAPIUtil {

    private static final String API_URL = "https://api.paladins.com/paladinsapi.svc";

    public static String formatURL(String method, int devId, String authKey, @Nullable String sessionId) {
        try {
            MessageDigest digestor = MessageDigest.getInstance("MD5");
            String timestamp = getTimeStamp();
            digestor.update((devId + method + authKey + timestamp).getBytes());
            byte[] bytes = digestor.digest();

            StringBuilder strBuilder = new StringBuilder();
            for (byte b : bytes) {
                strBuilder.append(String.format("%02x", b & 0xff));
            }
            if (Objects.isNull(sessionId)) {
                return String.format("%s/%sJson/%s/%s/%s", API_URL,
                        method, devId, strBuilder.toString(), timestamp);
            }

            return String.format("%s/%sJson/%s/%s/%s/%s", API_URL,
                    method, devId, strBuilder.toString(), sessionId, timestamp);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao formar a URL para a API", e);
        }
    }

    public static String formatURL(String method, int devId, String authKey, @Nullable String sessionId, Object... parameters) {
        return formatURL(method, devId, authKey, sessionId) + ((parameters.length != 0)
                ? "/" + Arrays.stream(parameters).map(Object::toString).collect(Collectors.joining("/"))
                : "");
    }

    private static String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        return sdf.format(new Date());
    }

}
