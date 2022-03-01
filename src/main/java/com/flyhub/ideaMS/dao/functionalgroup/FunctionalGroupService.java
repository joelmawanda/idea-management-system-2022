package com.flyhub.ideaMS.dao.functionalgroup;

import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMapping;
import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMappingService;
import com.flyhub.ideaMS.dao.module.Module;
import com.flyhub.ideaMS.dao.module.ModuleService;
import com.flyhub.ideaMS.exception.DuplicateDataException;
import com.flyhub.ideaMS.exception.GenericServiceException;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.utils.ServicesUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationType.GROUP;


/**
 *
 * @author Benjamin E Ndugga
 */

@Service
public class FunctionalGroupService {
    
    private static final Logger log = LogManager.getLogger(FunctionalGroupService.class);

    @Autowired
    private FunctionalGroupRepository functionalGroupRepository;

    @Autowired
    private EntityAuthorisationMappingService entityAuthorisationMappingService;

    @Autowired
    private ServicesUtils servicesUtils;

    @Autowired
    private ModuleService moduleService;

    public List<FunctionalGroup> listFunctionalGroups() {
        log.info("querying for all functional groups...");

        List<FunctionalGroup> functional_groups_list = functionalGroupRepository.findAll();

        log.info("found " + functional_groups_list.size() + " functional group(s)");

        List<FunctionalGroup> functional_groups_list_with_authorities;
        functional_groups_list_with_authorities = functional_groups_list
                .stream()
                .map((FunctionalGroup functionalGroup) -> {
                    functionalGroup.setModuleAuthorities(entityAuthorisationMappingService.fetchAllEntityMappingsByEntityIdAsHashMap(functionalGroup.getFunctionalGroupId()));
                    return functionalGroup;
                }).collect(Collectors.toList());

        return functional_groups_list_with_authorities;

    }

    public FunctionalGroup listFunctionalGroupByName(String name) throws RecordNotFoundException {

        log.info(String.format("list functional group by name... (%s)", name));

        FunctionalGroup functionalGroup = functionalGroupRepository.findByName(name).orElse(null);

        if (functionalGroup == null) {
            throw new RecordNotFoundException(2, String.format("Functional Group with Name: %s not Found.", name));
        }

        log.info(String.format("Found functional Group details: %s", functionalGroup));

        return functionalGroup;
    }

    public FunctionalGroup listFunctionalGroupById(String id) throws RecordNotFoundException {

        log.info("querying for Functional Group Id details for Id: " + id);

        FunctionalGroup functionalGroup = functionalGroupRepository.findById(id)
                .orElse(null);

        if (functionalGroup == null) {
            throw new RecordNotFoundException(2, String.format("Functional Group with id: %s not Found.", id));
        }

        log.info("functional group details: " + functionalGroup);

        functionalGroup.setModuleAuthorities(entityAuthorisationMappingService.fetchAllEntityMappingsByEntityIdAsHashMap(functionalGroup.getFunctionalGroupId()));

        return functionalGroup;

    }

    public FunctionalGroup createFunctionalGroup(FunctionalGroup functionalGroup) throws DuplicateDataException, GenericServiceException {

        log.info("creating functional group...");

        if (functionalGroupRepository.findByName(functionalGroup.getFunctionalGroupName().toUpperCase()).isPresent()) {
            throw new DuplicateDataException(2, "Functional Group Name: " + functionalGroup.getFunctionalGroupName() + " already exists.");
        }

        FunctionalGroup created_functional_group = functionalGroupRepository.save(functionalGroup);

        log.info("created functional group: " + created_functional_group);

        if (created_functional_group != null) {
            return created_functional_group;
        } else {
            throw new GenericServiceException(4, "Failed to create Functional Group with name: " + functionalGroup.getFunctionalGroupName());
        }
    }

    public FunctionalGroup updateFunctionalGroup(String id, FunctionalGroup functionalGroup) throws RecordNotFoundException, GenericServiceException {

        log.info("updating functional group...");

        //check if the functional group exists in the database
        FunctionalGroup old_functional_group = functionalGroupRepository.findById(id).orElse(null);

        if (old_functional_group == null) {

            throw new RecordNotFoundException(1, "Functional Group Id " + id + " does not exist!");

        }

        functionalGroup.setMerchants(old_functional_group.getMerchants());
        functionalGroup.setModules(old_functional_group.getModules());
        functionalGroup.setSysusers(old_functional_group.getSysusers());

        servicesUtils.copyNonNullProperties(functionalGroup, old_functional_group);

        FunctionalGroup edited_functional_group = functionalGroupRepository.save(old_functional_group);

        log.info("updated functional group: " + edited_functional_group);

        if (edited_functional_group == null) {

            throw new GenericServiceException(3, "Failed to create functional Group.");

        } else {

            return edited_functional_group;

        }

    }

    public int deleteFunctionalGroup(String id) throws RecordNotFoundException {

        log.info("delete request for functional group id: " + id);

        int number_of_deleted_rows = functionalGroupRepository.deleteByFunctionalGroupByIds(id);

        log.info("delete operation rows count: " + number_of_deleted_rows);

        if (number_of_deleted_rows <= 0) {

            throw new RecordNotFoundException(1, "Functional Group Id  " + id + " does not exist.");

        }

        return number_of_deleted_rows;
    }

    public FunctionalGroup addModuleToFunctionalGroup(String functionalGroupName, String modulename) throws RecordNotFoundException, DuplicateDataException, GenericServiceException {

        FunctionalGroup functionalGroup = functionalGroupRepository.findByName(functionalGroupName).orElse(null);

        if (functionalGroup == null) {
            log.info("Functional Group " + functionalGroupName + " does not exist");
            throw new RecordNotFoundException(1, "Functional Group " + functionalGroupName + " does not exist.");
        }

        Module module = moduleService.resolveSystemModuleName(modulename);

        if (module == null) {
            log.info("Module " + modulename + " does not exist");
            throw new RecordNotFoundException(1, "Module " + modulename + " does not exist.");
        }


        Set<Module> modules = functionalGroup.getModules();

        if (modules.contains(module)) {
            throw new DuplicateDataException(2, "This Module already exists");
        }

        modules.add(module);

        FunctionalGroup new_functionalGroup = functionalGroupRepository.save(functionalGroup);

        if (new_functionalGroup == null) {
            throw new GenericServiceException(3, "Failed to add module to the functional group");
        }

        EntityAuthorisationMapping eam = new EntityAuthorisationMapping();
        eam.setEntityId(new_functionalGroup.getFunctionalGroupId());
        eam.setEntityType(GROUP);
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

        //select all the functional group authorites
        new_functionalGroup.setModuleAuthorities(entityAuthorisationMappingService.fetchAllEntityMappingsByEntityIdAsHashMap(new_functionalGroup.getFunctionalGroupId()));

        return new_functionalGroup;

    }

    public FunctionalGroup removeModuleFromFunctionalGroup(String functionalGroupName, String moduleName) throws RecordNotFoundException, GenericServiceException {

        FunctionalGroup functionalGroup = functionalGroupRepository.findByName(functionalGroupName).orElse(null);

        if (functionalGroup == null) {
            throw new RecordNotFoundException(1, "Functional Group " + functionalGroupName + " does not exist.");

        }

        Module module = moduleService.resolveSystemModuleName(moduleName);

        if (module == null) {
            throw new RecordNotFoundException(1, "Module name " + moduleName + " does not exist.");
        }

        Set<Module> modules = functionalGroup.getModules();

        if (!modules.contains(module)) {
            throw new GenericServiceException(2, "This Module isn't assigned to functional group");
        }

        modules.remove(module);

        FunctionalGroup new_functionalGroup = functionalGroupRepository.save(functionalGroup);

        if (new_functionalGroup == null) {
            throw new GenericServiceException(3, "Failed to remove module from the functional group");
        }

        //we need to remove the mapping from the authorities table
        entityAuthorisationMappingService.removeModuleFromEntity(functionalGroup.getFunctionalGroupId(), module.getModuleId());

        return new_functionalGroup;
    }
}
