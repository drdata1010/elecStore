package com.soft.electronic.store.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    //security filter chain beans mandatory

    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        //configurations

        //urls which to be protected which to be not
        security.authorizeHttpRequests(request->{
            request.requestMatchers("/products").authenticated();
            request.anyRequest().permitAll();
        });

        //type of security to use
        //basic auth
        security.httpBasic(Customizer.withDefaults());
        return security.build();
    }
}
