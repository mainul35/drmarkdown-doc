package com.drmarkdown.doc.config.security;

import com.drmarkdown.doc.exceptions.InvalidPayloadException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class DocAuthFilter extends AbstractAuthenticationProcessingFilter {

    public DocAuthFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public DocAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String tokenUnstripped = request.getHeader(AUTHORIZATION);
        String token = StringUtils.removeStart(Optional.ofNullable(tokenUnstripped).orElse(""), "Bearer").trim();

        Authentication authentication;
        if (token.isEmpty()) {
            authentication = new UsernamePasswordAuthenticationToken("guest", "");
        } else {
            String[] splitToken = token.split("\\.");

            byte[] decodedBytes = Base64.getDecoder().decode(splitToken[1]);

            String payload = new String(decodedBytes, StandardCharsets.UTF_8);
            Map<String, String> map = this.mapPayload(payload);
            if (map == null || map.get("sub") == null) try {
                throw new InvalidPayloadException("Invalid payload");
            } catch (InvalidPayloadException e) {
                e.printStackTrace();
            }
            String username = map.get("sub");
            authentication = new UsernamePasswordAuthenticationToken(username, token);
        }
        return getAuthenticationManager().authenticate(authentication);
    }

    private Map mapPayload(String payload) {
        ObjectMapper mapper = new ObjectMapper();
        String json = payload;

        try {

            // convert JSON string to Map
            Map<String, String> map = mapper.readValue(json, Map.class);

            // it works
            //Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});

            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", failed.getCause());
        jsonObject.put("errorMessage", failed.getMessage());

        response.getWriter().print(jsonObject);
        response.getWriter().flush();
    }
}
