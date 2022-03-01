package com.flyhub.ideaMS.dao.module;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author Benjamin E Ndugga
 */
@Service
public class ModuleService {

    private static final Logger log = Logger.getLogger(ModuleService.class.getName());

    @Autowired
    private ModuleRepository moduleRepository;

    public List<Module> listAllModules() {

        log.info("querying for all modules...");

        List<Module> modules_list = moduleRepository.findAll();

        log.info("system supports " + modules_list.size() + " module(s)");

        return modules_list;
    }

    public List<Module> fetchAllModules() {
        log.info("querying for all modules...");
        return moduleRepository.findAll();
    }

    public Module resolveSystemModuleName(String moduleName) throws IllegalArgumentException {

        SystemModule systemModule = SystemModule.valueOf(moduleName);

        log.info("name resolved to enum " + systemModule);

        return moduleRepository.findByName(systemModule).get();

    }

    public Module resolveSystemModuleName(SystemModule systemModule) throws IllegalArgumentException {

        return moduleRepository.findByName(systemModule).get();

    }

    public void createSystemModule(SystemModule value) {

        moduleRepository.save(new Module(value, true));

    }

}
