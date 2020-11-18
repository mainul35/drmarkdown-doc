package com.drmarkdown.doc.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
                .authenticationProvider(authenticationProvider)
        .addFilterBefore(docAuthFilter(), AnonymousAuthenticationFilter.class)
        .authorizeRequests().anyRequest().authenticated()
                .and().csrf().disable()
                .httpBasic().disable()
                .logout().disable()
                .formLogin().disable()
                .cors();
    }

    private Filter docAuthFilter() throws Exception {
        OrRequestMatcher orRequestMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher("/doc/**")
        );
        DocAuthFilter authFIlter = new DocAuthFilter(orRequestMatcher);
        authFIlter.setAuthenticationManager(authenticationManager());
        return authFIlter;
    }
}
