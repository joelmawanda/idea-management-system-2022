package com.flyhub.ideaMS.dao.systemuser;

import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMapping;
import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMappingService;
import com.flyhub.ideaMS.dao.functionalgroup.FunctionalGroup;
import com.flyhub.ideaMS.dao.functionalgroup.FunctionalGroupRepository;
import com.flyhub.ideaMS.dao.module.ModuleService;
import com.flyhub.ideaMS.exception.DuplicateDataException;
import com.flyhub.ideaMS.exception.GenericServiceException;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.utils.ServicesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.flyhub.ideaMS.dao.module.Module;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationType.INDIVIDUAL;

@Service
public class SystemUserService {

    private static final Logger log = Logger.getLogger(SystemUserService.class.getName());

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private FunctionalGroupRepository functionalGroupRepository;

    @Autowired
    private EntityAuthorisationMappingService entityAuthorisationMappingService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ServicesUtils servicesUtils;

    public boolean checkSystemUserExists(String systemId) {
        return systemUserRepository.findBySystemId(systemId).isPresent();
    }

    public List<SystemUser> listSystemUsers() {
        log.info("querying for all system users...");

        List<SystemUser> all_sys_users = systemUserRepository.findAll();

        log.info("system found " + all_sys_users.size() + " system user(s)");

        List<SystemUser> all_sys_users_with_authorities = all_sys_users.stream().map((SystemUser systemUser) -> {
            systemUser.setAuthorities(entityAuthorisationMappingService.fetchAllEntityMappingsByEntityIdAsHashMap(systemUser.getSystemUserId()));
            return systemUser;
        }).collect(Collectors.toList());

        return all_sys_users_with_authorities;
    }

    public SystemUser listSystemUserById(String systemId) {
        log.info("find by system id " + systemId + "...");

        return systemUserRepository.findByUniqueId(systemId)
                .orElse(null);
    }

    public SystemUser listSystemUserByIDWithAuthDetails(String systemId) throws RecordNotFoundException {

        log.info("find by system id " + systemId + "...");

        SystemUser systemUser = systemUserRepository.findBySystemId(systemId)
                .orElse(null);

        log.info("system user info: " + systemUser);

        if (systemUser == null) {

            throw new RecordNotFoundException(1, String.format("System Id %s does not exist.", systemId));

        } else {

            systemUser.setAuthorities(entityAuthorisationMappingService.fetchAllEntityMappingsByEntityIdAsHashMap(systemId));

            return systemUser;
        }
    }

    public SystemUser createSystemUser(SystemUser systemUser) throws DuplicateDataException, GenericServiceException {

        //check if the unique parameters are new
        //if there is a user that has any of these parameters then
        // we can not proceed
        if (systemUserRepository.findByUniqueParameters(systemUser.getUserName(), systemUser.getEmail(), systemUser.getPhoneNumber()).isPresent()) {
            log.info("user data exists...");
            throw new DuplicateDataException(1, "User Creation failed. This data exists.");
        }

        if (!systemUser.getConfirmPassword().equals(systemUser.getPassword())) {
            throw new GenericServiceException(1, "Passwords do not match.");
        }

        systemUser.setPassword(encoder.encode(systemUser.getPassword()));

        log.info("create an api user...");

        SystemUser created_user = systemUserRepository.save(systemUser);

        if (created_user != null) {
            return created_user;
        } else {
            throw new GenericServiceException(1, "User Creation Failed.");
        }
    }

    public SystemUser updateSystemUser(String systemId, SystemUser systemUser) throws RecordNotFoundException, GenericServiceException {

        SystemUser old_system_user = systemUserRepository.findBySystemId(systemId).orElse(null);

        if (old_system_user == null) {
            throw new RecordNotFoundException(1, String.format("System User with Id: %s does not exist.", systemId));
        }

        systemUser.setFunctionalGroups(old_system_user.getFunctionalGroups());

        servicesUtils.copyNonNullProperties(systemUser, old_system_user);

        if (systemUser.getPassword() != null) {
            log.info("updating system user password...");
            old_system_user.setPassword(encoder.encode(systemUser.getPassword()));
        }

        SystemUser edited_system_user = systemUserRepository.save(old_system_user);

        if (edited_system_user != null) {
            return edited_system_user;
        } else {
            throw new GenericServiceException(1, "System User update failed.");
        }

    }

    public int deleteSystemUser(String sysid) throws RecordNotFoundException {

        log.info(String.format("Delete request for SystemId: %s", sysid));

        int number_of_deleted_rows = systemUserRepository.deleteSystemUsers(sysid);

        log.info("delete operation rows count: " + number_of_deleted_rows);

        if (number_of_deleted_rows <= 0) {

            throw new RecordNotFoundException(1, "System User with Id: " + sysid + " does not exist.");

        } else {

            return number_of_deleted_rows;
        }

    }

    public SystemUser addFunctionalGroupToSystemUser(String systemId, String functionalGroupName) throws RecordNotFoundException, DuplicateDataException, GenericServiceException {

        SystemUser systemUser = systemUserRepository.findBySystemId(systemId).orElse(null);

        if (systemUser == null) {
            throw new RecordNotFoundException(1, "System User with id: " + systemId + " does not exist.");
        }

        FunctionalGroup functionalGroup = functionalGroupRepository.findByName(functionalGroupName).orElse(null);

        if (functionalGroup == null) {
            throw new RecordNotFoundException(1, "Functional Group with name: " + functionalGroupName + " does not exist.");
        }

        Set<FunctionalGroup> fg = systemUser.getFunctionalGroups();

        if (fg.contains(functionalGroup)) {
            throw new DuplicateDataException(2, "Functional group with name " + functionalGroupName + " already exists");
        }

        fg.add(functionalGroup);

        SystemUser new_user = systemUserRepository.save(systemUser);

        if (new_user == null) {
            throw new GenericServiceException(3, "Failed to add Functional group to user");
        }

        return listSystemUserByIDWithAuthDetails(systemId);

    }

    public SystemUser removeFunctionalGroupFromSystemUser(String systemId, String functionalGroupName) throws RecordNotFoundException, DuplicateDataException, GenericServiceException {

        SystemUser user = systemUserRepository.findBySystemId(systemId)
                .orElse(null);

        if (user == null) {
            throw new RecordNotFoundException(1, "System User with Id: " + systemId + " does not exist.");

        }

        FunctionalGroup functionalGroup = functionalGroupRepository.findByName(functionalGroupName).orElse(null);

        if (functionalGroup == null) {
            throw new RecordNotFoundException(1, "Functional Group with name: " + functionalGroupName + " does not exist.");
        }

        Set<FunctionalGroup> fg = user.getFunctionalGroups();

        if (!fg.contains(functionalGroup)) {
            throw new DuplicateDataException(2, "Functional group is not assigned to user");
        }

        fg.remove(functionalGroup);

        SystemUser new_user = systemUserRepository.save(user);

        if (new_user == null) {
            throw new GenericServiceException(3, "Failed to remove Functional group from user");
        } else {
            return listSystemUserByIDWithAuthDetails(systemId);
        }
    }

    public SystemUser addModuleToSystemUser(String systemId, String moduleName) throws RecordNotFoundException, DuplicateDataException, GenericServiceException {

        try {
            //check if this system id exists
            if (!systemUserRepository.findBySystemId(systemId).isPresent()) {
                throw new RecordNotFoundException(1, String.format("System Id %s does not exist", systemId));
            }

            //check if this module name exists, this should throw and example
            Module module = moduleService.resolveSystemModuleName(moduleName);

            if (entityAuthorisationMappingService.mappingExists(systemId, moduleName)) {
                throw new DuplicateDataException(2, "Mapping between SystemID: " + systemId + " and Module Name: " + moduleName + " exists.");
            }

            //add entry into the entity authorities table
            EntityAuthorisationMapping eam = new EntityAuthorisationMapping();
            eam.setEntityId(systemId);
            eam.setEntityType(INDIVIDUAL);
            eam.setModuleId(module.getModuleId());
            eam.setModuleName(module.getSystemModule().name());

            eam.setAccessMode(Boolean.FALSE);
            eam.setSearchAllowed(Boolean.FALSE);
            eam.setCreateAllowed(Boolean.FALSE);
            eam.setReadAllowed(Boolean.FALSE);
            eam.setUpdateAllowed(Boolean.FALSE);
            eam.setDeleteAllowed(Boolean.FALSE);
            eam.setPrintAllowed(Boolean.FALSE);

            entityAuthorisationMappingService.createEntityAuthorisationMapping(eam);

            return listSystemUserByIDWithAuthDetails(systemId);

        } catch (IllegalArgumentException ex) {
            throw new GenericServiceException(1, String.format("Unknown Module Name %s", moduleName));
        }
    }

    public SystemUser removeModuleFromSystemUser(String systemId, String moduleName) throws RecordNotFoundException, GenericServiceException {
        try {

            //check if this system user id exists
            if (!systemUserRepository.findBySystemId(systemId).isPresent()) {
                throw new RecordNotFoundException(1, String.format("System User Id %s does not exist", systemId));
            }

            //check if this module name exists, this should throw and example
            Module module = moduleService.resolveSystemModuleName(moduleName);

            //check if this mapping is available
            if (!entityAuthorisationMappingService.mappingExists(systemId, moduleName)) {
                throw new RecordNotFoundException(1, String.format("Mapping between SystemUserId %s and Module %s doesn't exist.", systemId, moduleName));
            }

            Integer rows_deleted = entityAuthorisationMappingService.removeModuleFromEntity(systemId, module.getModuleId());

            if (rows_deleted < 1) {
                throw new GenericServiceException(1, String.format("Failed to delete mapping between SystemUserId %s and Module %s", systemId, moduleName));
            }

            return listSystemUserByIDWithAuthDetails(systemId);

        } catch (IllegalArgumentException ex) {

            throw new RecordNotFoundException(1, String.format("Unknown Module with Name: %s", moduleName));

        }
    }

}
