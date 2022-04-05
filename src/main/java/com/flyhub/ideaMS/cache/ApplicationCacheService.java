package com.flyhub.ideaMS.cache;

import com.flyhub.ideaMS.configurations.GlobalConfig;
import com.flyhub.ideaMS.dao.merchant.MerchantDetails;
import com.flyhub.ideaMS.dao.systemuser.SystemUserDetails;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 *
 * @author Joel Mawanda
 */
@Component
public class ApplicationCacheService {

    private static final Logger log = LogManager.getLogger(ApplicationCacheService.class);

    @Autowired
    private HazelcastInstance hazelcastInstance;

    public void cacheAuthenticationObject(Authentication authenticate) {
        log.info("caching the authentication object...");

        IMap<String, Authentication> map = hazelcastInstance.getMap(GlobalConfig.PRINICIPLE_OBJECT_CACHE_MAP_NAME);

        Object principal = authenticate.getPrincipal();

        if (principal.getClass().isAssignableFrom(SystemUserDetails.class)) {
            map.put(((SystemUserDetails) principal).getId(), authenticate);
        } else {
            map.put(((MerchantDetails) principal).getId(), authenticate);
        }
    }

    public Authentication fetchAuthenticationObject(String entityId) {

        log.info(String.format("fetching authentication object for user id %s ...", entityId));

        IMap<String, Authentication> map = hazelcastInstance.getMap(GlobalConfig.PRINICIPLE_OBJECT_CACHE_MAP_NAME);
        return map.get(entityId);
    }

}
