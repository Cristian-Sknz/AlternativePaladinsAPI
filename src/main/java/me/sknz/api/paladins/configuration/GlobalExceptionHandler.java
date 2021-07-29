package me.sknz.api.paladins.configuration;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.sknz.api.paladins.exception.PaladinsAPIException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(TokenExpiredException.class)
    public void handleTokenExpiredException(HttpServletResponse response, TokenExpiredException e) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("Authentication", "basic");
        node.put("Message", e.getMessage());
        node.put("Timestamp", OffsetDateTime.now(Clock.systemUTC())
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        mapper.writeValue(response.getWriter(), node);
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public void handleAPISocketTimeoutException(HttpServletResponse response, SocketTimeoutException e) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("Exception", e.getMessage());
        node.put("Message", "There was a problem connecting to the Paladins API. PaladinsAPI took time to send a response.");
        node.put("Timestamp", OffsetDateTime.now(Clock.systemUTC())
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        response.setContentType("application/json");
        response.setStatus(HttpStatus.GATEWAY_TIMEOUT.value());
        mapper.writeValue(response.getWriter(), node);
    }

    @ExceptionHandler(PaladinsAPIException.class)
    public void handlePaladinsAPIException(HttpServletResponse response, PaladinsAPIException e) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("Authentication", "Bearer");
        node.put("Message", e.getMessage());
        node.put("Timestamp", OffsetDateTime.now(Clock.systemUTC())
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        response.setContentType("application/json");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        mapper.writeValue(response.getWriter(), node);
    }


    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  HttpStatus status, @NotNull WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now());
        body.put("status", status.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> messageSource.getMessage(Objects.requireNonNull(x.getDefaultMessage(), "validation message is null"), null, Locale.getDefault()))
                .collect(Collectors.toList());

        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

}
