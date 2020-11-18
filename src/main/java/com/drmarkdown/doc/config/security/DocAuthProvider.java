package com.drmarkdown.doc.config.security;

import com.drmarkdown.doc.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocAuthProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private TokenService tokenService;
    
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();

        if (token.isEmpty()) {
            return new User(username, "", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        } else {
            List<String> roles = tokenService.getUserRoles((String) authentication.getCredentials());
            return new User(username, "",
                    AuthorityUtils.createAuthorityList(roles.stream().map(s -> "ROLE_" + s).toArray(String[]::new))
            );
        }
    }
}
