package com.flyhub.ideaMS;

import com.flyhub.ideaMS.dao.module.ModuleService;
import com.flyhub.ideaMS.dao.module.SystemModule;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 *
 * @author Benjamin E Ndugga
 */
@Order(1)
@Component
public class DefaultSecurityConfigurationData implements CommandLineRunner {

    private static final Logger log = Logger.getLogger(DefaultSecurityConfigurationData.class.getName());

    @Autowired
    private ModuleService moduleService;

    @Override
    public void run(String... args) throws Exception {

        //check if the supported module count is equal to the number of modules loaded
        if (moduleService.fetchAllModules().size() != SystemModule.values().length) {
            log.info("loading default security configuration...");
            //load the support modules and by default they are all enabled
            for (SystemModule value : SystemModule.values()) {
                moduleService.createSystemModule(value);
            }
        }

    }
}
