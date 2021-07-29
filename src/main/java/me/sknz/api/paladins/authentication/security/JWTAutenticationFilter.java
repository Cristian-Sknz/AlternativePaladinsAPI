package me.sknz.api.paladins.authentication.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.sknz.api.paladins.authentication.data.JWTUserDetailsServiceImpl;
import me.sknz.api.paladins.authentication.model.APIUserModel;
import me.sknz.api.paladins.authentication.model.JWTAuthenticatedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class JWTAutenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static String TEMPORARY_TOKEN = "dcb231c1-6c71-43ff-91dc-9c66b6ce7e5d";
    private final JWTUserDetailsServiceImpl userDetailsService;

    public JWTAutenticationFilter(JWTUserDetailsServiceImpl userDetailsService, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String[] credentials = credentials(request.getParameterMap());
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(userDetailsService.loadUserByUsername(credentials[0]), credentials[1], new ArrayList<>()));
    }

    public String[] credentials(Map<String, String[]> parameters){
        if (parameters.keySet().stream().noneMatch(param ->
                param.equalsIgnoreCase("developerId") || param.equalsIgnoreCase("password"))) {
            return new String[]{"", ""};
        }
        return new String[] {parameters.get("developerId")[0], parameters.get("password")[0]};
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        if (failed instanceof BadCredentialsException){
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("Authentication", "basic");
            node.put("Message", failed.getMessage());
            node.put("Timestamp", OffsetDateTime.now(Clock.systemUTC())
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            mapper.writeValue(response.getWriter(), node);
            return;
        }
        super.unsuccessfulAuthentication(request, response, failed);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        APIUserModel userDetails = (APIUserModel) authResult.getPrincipal();
        String token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()
                        + 600_000))
                .sign(Algorithm.HMAC512(TEMPORARY_TOKEN));

        response.setContentType("application/json");
        response.setStatus(HttpStatus.ACCEPTED.value());
        new ObjectMapper().writeValue(response.getWriter(), new JWTAuthenticatedModel(token, 600_000));
    }
}
