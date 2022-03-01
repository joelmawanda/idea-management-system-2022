package com.flyhub.ideaMS.cache;

import com.flyhub.ideaMS.configurations.GlobalConfig;
import com.hazelcast.config.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *
 * @author Benjamin E Ndugga
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    
    private static final Logger log = Logger.getLogger(CacheConfiguration.class.getName());

    @Value("${cache.service.port}")
    private int cacheServicePort;

    @Value("${cache.service.name}")
    private String clusterName;
    
    @Value("${cache.service.loggingtype}")
    private String cacheLoggingType;

    @Value("#{'${cache.service.members}'.split(',')}")
    private List<String> members;

    @Bean
    public Config configureCacheProperties() {

        log.info("configuring cache service with name: " + clusterName + " ...");

        Config config = new Config();
        config.setClusterName(clusterName);
        config.setProperty("hazelcast.logging.type", cacheLoggingType);

        config.getNetworkConfig()
                .setJoin(new JoinConfig()
                        .setMulticastConfig(new MulticastConfig().setEnabled(false))
                        .setTcpIpConfig(new TcpIpConfig().setEnabled(true).setMembers(members)))
                .setPort(cacheServicePort)
                .setPortAutoIncrement(true);

        /**
         * set up the cache MAP for the principle objects
         */
        MapConfig mapConfig = new MapConfig();
        mapConfig.setName(GlobalConfig.PRINICIPLE_OBJECT_CACHE_MAP_NAME)
                .setBackupCount(1)
                .setTimeToLiveSeconds(86400);

        config.addMapConfig(mapConfig);
        return config;
    }

}
