package com.drmarkdown.doc.services.impl;

import com.drmarkdown.doc.services.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TokenServiceImpl implements TokenService {
    @Override
    public String getUserId(String jwtToken) {
        if (jwtToken.isEmpty()) {
            return StringUtils.EMPTY;
        }

        String claims = new String(Base64.getUrlDecoder().decode(jwtToken.split("\\.")[1]));
        JSONObject claimsJson = new JSONObject(claims);
        return (String) claimsJson.get("iss");
    }

    @Override
    public List<String> getUserRoles(String jwtToken) {
        String claims = new String(Base64.getUrlDecoder().decode(jwtToken.split("\\.")[1]));
        JSONObject claimsJson = new JSONObject(claims);

        String audience = claimsJson.getString("aud");
        final String[] split = audience
                .replace("[", "")
                .replace("]", "")
                .split(",");
        return Stream.of(split).map(String::trim).collect(Collectors.toList());
    }
}
