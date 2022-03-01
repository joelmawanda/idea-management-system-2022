package com.flyhub.ideaMS.security;

import com.flyhub.ideaMS.cache.ApplicationCacheService;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author Benjamin E Ndugga
 */
@Component
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = Logger.getLogger(JwtTokenAuthenticationFilter.class.getName());

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ApplicationCacheService applicationCacheService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Strings.isNullOrEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            //LOGGER.info("Authorization Header missing");
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();

        if (!jwtTokenUtil.validate(token)) {
            chain.doFilter(request, response);
            return;
        }

        String userId = jwtTokenUtil.getUserId(token);

        Authentication authentication = applicationCacheService.fetchAuthenticationObject(userId);

        if (authentication == null) {
            log.info("no authenticated data found!");
            chain.doFilter(request, response);
            return;
        }

        log.info(String.format("setting up the principle object on SecurityContext: " + authentication));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

}
