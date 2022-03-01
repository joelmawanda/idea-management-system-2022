package com.flyhub.ideaMS.dao.entityauthority;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Benjamin E Ndugga
 */
@Repository
public interface EntityAuthorisationMappingRepository extends JpaRepository<EntityAuthorisationMapping, Long> {

    @Query("SELECT s FROM entity_authority_mappings s WHERE s.entityId=:id")
    public List<EntityAuthorisationMapping> findByEntityId(@Param("id") String id);

    @Query("SELECT s FROM entity_authority_mappings s WHERE s.entityId IN (:ids)")
    public List<EntityAuthorisationMapping> findByEntityIds(@Param("ids") List<String> ids);

    @Query("SELECT s FROM entity_authority_mappings s WHERE s.entityId=:entityId AND s.moduleId=:moduleId")
    public Optional<EntityAuthorisationMapping> findByEntityIdAndModuleId(@Param("entityId") String entityId, @Param("moduleId") String moduleId);

    @Query("SELECT s FROM entity_authority_mappings s WHERE s.entityId=:entityId AND s.moduleName=:moduleName")
    public Optional<EntityAuthorisationMapping> findByEntityIdAndModuleName(@Param("entityId") String entityId, @Param("moduleName") String moduleName);

    @Modifying
    @Transactional
    @Query("DELETE FROM entity_authority_mappings s WHERE s.entityId=:id")
    public Integer deleteEntityMappingEntiesByEntityId(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("DELETE FROM entity_authority_mappings s WHERE s.entityId=:entityId AND s.moduleId=:moduleId")
    public Integer deleteEntityModuleMapping(@Param("entityId") String entityId, @Param("moduleId") String moduleId);

}
