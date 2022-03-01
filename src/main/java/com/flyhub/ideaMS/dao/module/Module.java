package com.flyhub.ideaMS.dao.module;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.flyhub.ideaMS.dao.StringPrefixedSequenceIdGenerator;
import com.flyhub.ideaMS.dao.functionalgroup.FunctionalGroup;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.flyhub.ideaMS.dao.DaoConstants.FUNCTIONAL_GROUP_MAPPINGS_TABLE;


@Entity(name = "modules")
@Table(name = "modules")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = "functional_groups")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Module implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "module_seq")
    @GenericGenerator(name = "module_seq", strategy = "com.flyhub.ideaMS.dao.StringPrefixedSequenceIdGenerator",
            parameters = {
                @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "50"),
                @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "MOD_"),
                @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%d")})

    @Column(name = "module_id")
    private String moduleId;

    @Column(name = "module_name")
    @Enumerated(EnumType.STRING)
    private SystemModule systemModule;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = FUNCTIONAL_GROUP_MAPPINGS_TABLE,
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "func_id"),
            foreignKey = @ForeignKey(name = "func_id_fk"))
    private Set<FunctionalGroup> functionalGroups = new HashSet<>();

    public Module(SystemModule systemModule, boolean enabled) {
        this.systemModule = systemModule;
        this.enabled = enabled;
    }

    public Set<FunctionalGroup> getFunctionalGroups() {
        return functionalGroups;
    }

    public void setFunctionalGroups(Set<FunctionalGroup> functionalGroups) {
        this.functionalGroups.clear();
        this.functionalGroups.addAll(functionalGroups);
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public SystemModule getSystemModule() {
        return systemModule;
    }

    public void setSystemModule(SystemModule systemModule) {
        this.systemModule = systemModule;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Module{" + "moduleId=" + moduleId + ", systemModule=" + systemModule + ", enabled=" + enabled + '}';
    }

}
