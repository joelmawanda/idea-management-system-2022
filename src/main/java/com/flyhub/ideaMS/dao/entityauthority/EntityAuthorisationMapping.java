package com.flyhub.ideaMS.dao.entityauthority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "entity_authority_mappings")
@Table(name = "entity_authority_mappings")
@JsonIgnoreProperties(value = {"id", "entity_type", "entity_id", "module_id", "module_name"}, allowSetters = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EntityAuthorisationMapping implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "entity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityAuthorisationType entityType;

    @Column(name = "entity_id", nullable = false)
    private String entityId;

    @Column(name = "module_id", nullable = false)
    private String moduleId;

    @Column(name = "module_name", nullable = false)
    private String moduleName;

    @Column(name = "search_allowed")
    private Boolean searchAllowed;

    @Column(name = "create_allowed")
    private Boolean createAllowed;

    @Column(name = "read_allowed")
    private Boolean readAllowed;

    @Column(name = "update_allowed")
    private Boolean updateAllowed;

    @Column(name = "delete_allowed")
    private Boolean deleteAllowed;

    @Column(name = "print_allowed")
    private Boolean printAllowed;

    //by default all rules are enabled
    @Column(name = "access_mode")
    private Boolean accessMode;

    public EntityAuthorisationMapping(EntityAuthorisationType entityType, String entityId, String moduleId, String moduleName) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.moduleId = moduleId;
        this.moduleName = moduleName;
    }

    public EntityAuthorisationMapping(Boolean searchAllowed, Boolean createAllowed, Boolean readAllowed, Boolean updateAllowed, Boolean deleteAllowed, Boolean printAllowed, Boolean accessMode) {
        this.searchAllowed = searchAllowed;
        this.createAllowed = createAllowed;
        this.readAllowed = readAllowed;
        this.updateAllowed = updateAllowed;
        this.deleteAllowed = deleteAllowed;
        this.printAllowed = printAllowed;
        this.accessMode = accessMode;
    }

}
