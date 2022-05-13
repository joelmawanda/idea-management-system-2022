package com.flyhub.ideaMS.security;

import com.flyhub.ideaMS.dao.merchant.MerchantDetailService;
import com.flyhub.ideaMS.dao.systemuser.SystemUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MerchantDetailService merchantDetailService;

    @Autowired
    private SystemUserDetailService systemUserDetailService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;

    private static final String[] AUTH_WHITELIST = {
        //-- public endpoints --
        "/api/v1/auth/test",
            "/api/v1/auth/login",
            "/api/v1/auth/requesttoken",
            "/api/v1/config/**",
        "/api/v1/suggestions/**"
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(merchantDetailService).passwordEncoder(encoder);
        auth.userDetailsService(systemUserDetailService).passwordEncoder(encoder);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        //Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                        }
                ).and();

        // Set permissions on endpoints
        http = http.authorizeRequests()
                // Our public endpoints
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and();

        //http.httpBasic().realmName("Flyhub App");
        http.addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
