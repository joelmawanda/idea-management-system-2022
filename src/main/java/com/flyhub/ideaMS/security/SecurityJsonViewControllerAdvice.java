package com.flyhub.ideaMS.security;

import com.flyhub.ideaMS.dao.systemuser.SystemUserDetails;
import com.flyhub.ideaMS.models.views.View;
import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

/**
 * this class is responsible for filtering JSON serialization output depending
 * on they type of user this is API USER or SYSTEM USER
 *
 * @author Benjamin E Ndugga
 */
@RestControllerAdvice
public class SecurityJsonViewControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    private static final Logger log = Logger.getLogger(SecurityJsonViewControllerAdvice.class.getName());

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue mjv, MediaType mt, MethodParameter mp, ServerHttpRequest shr, ServerHttpResponse shr1) {

        log.info("setting json view...");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getClass().isAssignableFrom(SystemUserDetails.class)) {
            log.info("setting sys admin view");
            mjv.setSerializationView(View.SystemAdminView.class);
        } else {
            log.info("setting api user view");
            mjv.setSerializationView(View.MerchantView.class);
        }
    }
}
