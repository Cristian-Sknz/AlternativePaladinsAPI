package me.sknz.api.paladins.authentication.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class JWTValidationFilter extends BasicAuthenticationFilter {

    public static final String HEADER = "Authorization";
    public static final String PREFIX = "Bearer ";

    private HandlerExceptionResolver resolver;

    public JWTValidationFilter(AuthenticationManager authenticationManager, HandlerExceptionResolver resolver) {
        super(authenticationManager);
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String authorization = request.getHeader(HEADER);
        if (Objects.isNull(authorization) || !authorization.startsWith(PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(PREFIX.length());
        try {
            UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);
        } catch (TokenExpiredException e){
            resolver.resolveException(request, response, null, e);
        }
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String token){
        String user = JWT.require(Algorithm.HMAC512(JWTAutenticationFilter.TEMPORARY_TOKEN))
                .build()
                .verify(token).getSubject();

        return (Objects.isNull(user)) ? null : new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    }
}
