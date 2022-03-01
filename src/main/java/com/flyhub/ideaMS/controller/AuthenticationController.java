package com.flyhub.ideaMS.controller;

import com.flyhub.ideaMS.cache.ApplicationCacheService;
import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMappingDto;
import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMappingService;
import com.flyhub.ideaMS.dao.merchant.Merchant;
import com.flyhub.ideaMS.dao.merchant.MerchantDetails;
import com.flyhub.ideaMS.dao.merchant.MerchantService;
import com.flyhub.ideaMS.dao.systemuser.SystemUserDetails;
import com.flyhub.ideaMS.exception.DuplicateDataException;
import com.flyhub.ideaMS.exception.GenericServiceException;
import com.flyhub.ideaMS.models.AuthRequest;
import com.flyhub.ideaMS.models.DataObjectResponse;
import com.flyhub.ideaMS.models.OperationResponse;
import com.flyhub.ideaMS.security.JwtTokenUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Benjamin E Ndugga
 */

@RestController
@CrossOrigin(origins = {"${app.api.settings.cross-origin.urls}"})
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    
    private static final Logger log = Logger.getLogger(AuthenticationController.class.getName());
    

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private EntityAuthorisationMappingService entityAuthorisationMappingService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ApplicationCacheService applicationCacheService;

    @GetMapping("/test")
    public String test() {
        return "It Works!";
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEntityId(), authRequest.getPassword()));

            return "Principal Object: " + authenticate;

        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
            return ex.getLocalizedMessage();
        }
    }

    @PostMapping("/requesttoken")
    public ResponseEntity<?> requestToken(@RequestBody AuthRequest authRequest) {
        try {
            log.info("request for JWT token.");

            log.info("authenticate user credentials...");

            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEntityId(), authRequest.getPassword()));

            log.info("username and password authenticated...");

            Object principal = authenticate.getPrincipal();

            applicationCacheService.cacheAuthenticationObject(authenticate);

            String jwtToken;

            if (principal.getClass().isAssignableFrom(MerchantDetails.class)) {
                jwtToken = jwtTokenUtil.generateAccessToken((MerchantDetails) principal);
            } else {
                jwtToken = jwtTokenUtil.generateAccessToken((SystemUserDetails) principal);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, jwtToken)
                    .body(new DataObjectResponse(1, "Authenticated Successfully", jwtToken));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new DataObjectResponse(401, "Authentication failed", ex.getLocalizedMessage()));
        }
    }

    @PostMapping("/merchant/register")
    public ResponseEntity<?> createMerchant(@Valid @RequestBody Merchant merchant) {
        try {
            Merchant created_merchant = merchantService.createMerchant(merchant);

            if (created_merchant != null) {
                return new ResponseEntity<>(created_merchant, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(created_merchant, HttpStatus.EXPECTATION_FAILED);
            }
        } catch (DuplicateDataException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/checkauthorities")
    public HashMap<String, EntityAuthorisationMappingDto> checkEntityAuthority(@RequestBody List<String> ids) {
        return entityAuthorisationMappingService.fetchAllEntityMappingsByEntityIdAsHashMap(ids);
    }

}
