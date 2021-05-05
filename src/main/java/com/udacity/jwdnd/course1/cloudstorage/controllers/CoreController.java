package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.JwtUser;
import com.udacity.jwdnd.course1.cloudstorage.model.JwtUserBuilder;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

public abstract class CoreController {

    static final String CLAIM_KEY_IS_ENABLED = "isEnabled";
    static final String CLAIM_KEY_FIRSTNAME = "firstName";
    static final String CLAIM_KEY_LASTNAME = "lastName";

    @Autowired
    UserMapper userMapper;


    @Value("${jwt.header}")
    private String jwtHeader;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;


    protected User getCurrentUser(Map<String, String> headers) {
        String token = getToken(headers);
        return userMapper.getUser(getUserDetails(token).getUsername());
    }

    protected String getToken(Map<String, String> headers) {
        return headers.getOrDefault(jwtHeader.toLowerCase(), null);
    }

    public JwtUser getUserDetails(String token) {

        if (token == null) {
            return null;
        }
        try {
            final Claims claims = getClaimsFromToken(token);
            List<GrantedAuthority> authorities = null;


            return new JwtUserBuilder()
                    .setUsername(claims.getSubject())
                    .setPassword("")
                    .setFirstName((String) claims.get(CLAIM_KEY_FIRSTNAME))
                    .setLastName((String) claims.get(CLAIM_KEY_LASTNAME))
                    .setAuthorities(null)
                    .setEnabled((boolean) claims.get(CLAIM_KEY_IS_ENABLED))
                    .createJwtUser();


        } catch (Exception e) {
            return null;
        }
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public String getUsername () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        return null;
    }
}
