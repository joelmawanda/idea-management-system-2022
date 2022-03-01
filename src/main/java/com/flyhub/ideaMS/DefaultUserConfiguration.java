package com.flyhub.ideaMS;

import com.flyhub.ideaMS.dao.Gender;
import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMapping;
import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMappingService;
import com.flyhub.ideaMS.dao.module.Module;
import com.flyhub.ideaMS.dao.module.ModuleService;
import com.flyhub.ideaMS.dao.systemuser.SystemUser;
import com.flyhub.ideaMS.dao.systemuser.SystemUserRepository;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author Benjamin E Ndugga
 */
@Order(2)
@Component
public class DefaultUserConfiguration implements CommandLineRunner {

    private static final Logger log = Logger.getLogger(DefaultUserConfiguration.class.getName());

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private EntityAuthorisationMappingService entityAuthorisationMappingService;

    @Override
    public void run(String... args) throws Exception {

        log.debug("checking if there is a sys user");

        if (systemUserRepository.count() >= 1) {
            log.debug("found system users. stop commandline runner...");
            return;
        }

        log.info("loading default sys user configuration...");

        SystemUser systemUser = systemUserRepository.save(new SystemUser("Admin", "Administrator", "Mr", "II", "UGANDA", Gender.MALE, "admin", encoder.encode("pass"), encoder.encode("pass"), "admin@flyhub.com", "0703775706", "04-08-1996", true));

        List<Module> modules = moduleService.fetchAllModules();

        modules.stream().map(module -> {
            entityAuthorisationMappingService.createEntityAuthorisationMappingBetweenSystemUserandModule(systemUser, module);
            return module;
        }).forEachOrdered(module -> {
            try {
                entityAuthorisationMappingService.modifyEntityAuthorities(systemUser.getSystemUserId(), module.getSystemModule().name(), new EntityAuthorisationMapping(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
            } catch (RecordNotFoundException ex) {
                log.error(ex.getExceptionMessage());
            }
        });
    }
}
