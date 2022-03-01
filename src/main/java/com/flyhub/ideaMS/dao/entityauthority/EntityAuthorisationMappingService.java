package com.flyhub.ideaMS.dao.entityauthority;

import com.flyhub.ideaMS.dao.functionalgroup.FunctionalGroup;
import com.flyhub.ideaMS.dao.merchant.Merchant;
import com.flyhub.ideaMS.dao.module.Module;
import com.flyhub.ideaMS.dao.systemuser.SystemUser;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.utils.ServicesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationType.GROUP;
import static com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationType.INDIVIDUAL;


/**
 *
 * @author Benjamin E Ndugga
 */
@Service
public class EntityAuthorisationMappingService {

    private static final Logger log = Logger.getLogger(EntityAuthorisationMappingService.class.getName());

    @Autowired
    private ServicesUtils servicesUtils;

    @Autowired
    private EntityAuthorisationMappingRepository entityAuthorisationMappingRepository;

    public EntityAuthorisationMapping createEntityAuthorisationMapping(EntityAuthorisationMapping eam) {

        return entityAuthorisationMappingRepository.save(eam);

    }

    public EntityAuthorisationMapping createEntityAuthorisationMappingBetweenGroupAndModule(FunctionalGroup functionalGroup, Module module) {

        EntityAuthorisationMapping eam = new EntityAuthorisationMapping();
        eam.setEntityId(functionalGroup.getFunctionalGroupId());
        eam.setEntityType(GROUP);
        eam.setModuleId(module.getModuleId());
        eam.setModuleName(module.getSystemModule().name());

        return entityAuthorisationMappingRepository.save(eam);

    }

    public void createEntityAuthorisationMappingBetweenGroupAndModule(FunctionalGroup functionalGroup, List<Module> modules) {

        modules.forEach((Module module) -> {
            EntityAuthorisationMapping eam = new EntityAuthorisationMapping();
            eam.setEntityId(functionalGroup.getFunctionalGroupId());
            eam.setEntityType(GROUP);
            eam.setModuleId(module.getModuleId());
            eam.setModuleName(module.getSystemModule().name());

            entityAuthorisationMappingRepository.save(eam);
        });

    }

    public EntityAuthorisationMapping createEntityAuthorisationMappingBetweenMerchantAndModule(Merchant merchant, Module module) {

        EntityAuthorisationMapping eam = new EntityAuthorisationMapping();
        eam.setEntityId(merchant.getMerchantId());
        eam.setEntityType(INDIVIDUAL);
        eam.setModuleId(module.getModuleId());
        eam.setModuleName(module.getSystemModule().name());

        return entityAuthorisationMappingRepository.save(eam);
    }

    public EntityAuthorisationMapping createEntityAuthorisationMappingBetweenSystemUserandModule(SystemUser systemUser, Module module) {

        EntityAuthorisationMapping eam = new EntityAuthorisationMapping();
        eam.setEntityId(systemUser.getSystemUserId());
        eam.setEntityType(INDIVIDUAL);
        eam.setModuleId(module.getModuleId());
        eam.setModuleName(module.getSystemModule().name());

        return entityAuthorisationMappingRepository.save(eam);
    }

    public List<EntityAuthorisationMapping> fetchAllEntityMappingsByEntityId(String entityId) {
        return entityAuthorisationMappingRepository.findByEntityId(entityId);

    }

    public HashMap<String, EntityAuthorisationMappingDto> fetchAllEntityMappingsByEntityIdAsHashMap(String entityId) {

        HashMap<String, EntityAuthorisationMappingDto> map = new HashMap<>();

        fetchAllEntityMappingsByEntityId(entityId).forEach(eam -> {
            map.put(eam.getModuleName(), new EntityAuthorisationMappingDto(eam.getSearchAllowed(), eam.getCreateAllowed(), eam.getReadAllowed(), eam.getUpdateAllowed(), eam.getDeleteAllowed(), eam.getPrintAllowed(), eam.getAccessMode()));
        });
        return map;
    }

    public EntityAuthorisationHashMap fetchAllEntityMappingsByEntityIdAsHashMap(List<String> entityIds) {
        List<EntityAuthorisationMapping> entity_authorities = entityAuthorisationMappingRepository.findByEntityIds(entityIds);

        List<EntityAuthorisationMapping> individual_authorities = new ArrayList<>();

        List<EntityAuthorisationMapping> entity_authorities_to_be_removed = new ArrayList<>();

        entity_authorities
                .stream()
                .filter(entity_authority -> (entity_authority.getEntityType() == INDIVIDUAL))
                .forEachOrdered(entity_authority -> {
                    individual_authorities.add(entity_authority);
                });

        entity_authorities.forEach(entity_authority -> {
            individual_authorities
                    .stream()
                    .filter(individual_authority -> (entity_authority.getModuleName().equals(individual_authority.getModuleName()) && entity_authority.getEntityType() == GROUP))
                    .forEachOrdered(_item -> {
                        entity_authorities_to_be_removed.add(entity_authority);
                    });
        });

        entity_authorities.removeAll(entity_authorities_to_be_removed);

        //create a hash map
        EntityAuthorisationHashMap map = new EntityAuthorisationHashMap();

        entity_authorities.forEach(eam -> {
            map.put(eam.getModuleName(), new EntityAuthorisationMappingDto(eam.getSearchAllowed(), eam.getCreateAllowed(), eam.getReadAllowed(), eam.getUpdateAllowed(), eam.getDeleteAllowed(), eam.getPrintAllowed(), eam.getAccessMode()));
        });

        return map;
    }

    public HashMap<String, EntityAuthorisationMappingDto> modifyEntityAuthorities(String entityId, String modulename, EntityAuthorisationMapping entityAuthorisationMapping) throws RecordNotFoundException {

        //fetch entity mapping
        Optional<EntityAuthorisationMapping> entity_authority_optional = entityAuthorisationMappingRepository.findByEntityIdAndModuleName(entityId, modulename);

        if (!entity_authority_optional.isPresent()) {
            throw new RecordNotFoundException(1, String.format("No Entity Authority Mapping found for Entity %s and ModuleName %s", entityId, modulename));
        }

        EntityAuthorisationMapping old_eam_info = entity_authority_optional.get();

        servicesUtils.copyNonNullProperties(entityAuthorisationMapping, old_eam_info);

        //save changes
        entityAuthorisationMappingRepository.save(old_eam_info);

        //fetch updated infor on this entity ID
        //return functionalGroupService.listFunctionalGroupById(entityId);        
        return fetchAllEntityMappingsByEntityIdAsHashMap(entityId);
    }

    public boolean mappingExists(String entityId, String modulename) {
        return entityAuthorisationMappingRepository.findByEntityIdAndModuleName(entityId, modulename).isPresent();
    }

    public Integer removeModuleFromEntity(String entityId, String moduleId) {

        Integer rows_deleted = entityAuthorisationMappingRepository.deleteEntityModuleMapping(entityId, moduleId);

        log.info("rows-deleted: " + rows_deleted);

        return rows_deleted;
    }
}
