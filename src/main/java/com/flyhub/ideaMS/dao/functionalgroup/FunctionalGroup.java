package com.flyhub.ideaMS.dao.functionalgroup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.flyhub.ideaMS.dao.StringPrefixedSequenceIdGenerator;
import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMappingDto;
import com.flyhub.ideaMS.dao.merchant.Merchant;
import com.flyhub.ideaMS.dao.module.Module;
import com.flyhub.ideaMS.dao.systemuser.SystemUser;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.flyhub.ideaMS.dao.DaoConstants.FUNCTIONAL_GROUP_MAPPINGS_TABLE;

@Entity(name = "functional_groups")
@Table(name = "functional_groups")
@JsonIgnoreProperties(value = {"merchants", "sysusers", "modules"}, allowSetters = false)
@JsonPropertyOrder({"functional_group_id", "functional_group_name", "functional_group_code", "description", "enabled", "module_authorities"})
@EntityListeners(FunctionalGroupEntityListener.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FunctionalGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "func_grp_seq")
    @GenericGenerator(name = "func_grp_seq", strategy = "com.flyhub.ideaMS.dao.StringPrefixedSequenceIdGenerator",
            parameters = {
                @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "50"),
                @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "FUNC_"),
                @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%02d_GRP")})

    @Column(name = "func_id")
    private String functionalGroupId;

    @Column(name = "func_name", unique = true)
    @NotBlank(message = "Functional group name cannot be null")
    @Pattern(regexp = "^\\S*$", message = "Functional group name cannot contain spaces")
    private String functionalGroupName;

    @Column(name = "func_code", unique = true)
    @NotBlank(message = "Functional Group Code cannot be null")
    @Pattern(regexp = "^\\S*$", message = "Functional group code cannot contain spaces")
    private String functionalGroupCode;

    @Column(name = "func_desc")
    @NotNull(message = "Functional Group Description cannot be null. Property name: func_desc")
    private String description;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = FUNCTIONAL_GROUP_MAPPINGS_TABLE,
            joinColumns = @JoinColumn(name = "func_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id"),
            foreignKey = @ForeignKey(name = "module_id_fk"))
    private Set<Module> modules = new HashSet<>();

    @ManyToMany(mappedBy = "functionalGroups", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private Set<Merchant> merchants = new HashSet<>();

    @ManyToMany(mappedBy = "functionalGroups", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private Set<SystemUser> sysusers = new HashSet<>();

    @Transient
    private HashMap<String, EntityAuthorisationMappingDto> moduleAuthorities;

    public FunctionalGroup() {
    }

    public FunctionalGroup(String functionalGroupName, String functionalGroupCode, String description) {
        this.functionalGroupName = functionalGroupName;
        this.functionalGroupCode = functionalGroupCode;
        this.description = description;
    }

    public HashMap<String, EntityAuthorisationMappingDto> getModuleAuthorities() {
        return moduleAuthorities;
    }

    public void setModuleAuthorities(HashMap<String, EntityAuthorisationMappingDto> moduleAuthorities) {
        this.moduleAuthorities = moduleAuthorities;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public void setModules(Set<Module> modules) {
        this.modules.clear();
        this.modules.addAll(modules);
    }

    public Set<Merchant> getMerchants() {
        return merchants;
    }

    public void setMerchants(Set<Merchant> merchants) {
        this.merchants.clear();
        this.merchants.addAll(merchants);
    }

    public String getFunctionalGroupId() {
        return functionalGroupId;
    }

    public Set<SystemUser> getSysusers() {
        return sysusers;
    }

    public void setSysusers(Set<SystemUser> sysusers) {
        this.sysusers.clear();
        this.sysusers.addAll(sysusers);
    }

    public void setFunctionalGroupId(String functionalGroupId) {
        this.functionalGroupId = functionalGroupId;
    }

    public String getFunctionalGroupName() {
        return functionalGroupName;
    }

    public void setFunctionalGroupName(String functionalGroupName) {
        this.functionalGroupName = functionalGroupName;
    }

    public String getFunctionalGroupCode() {
        return functionalGroupCode;
    }

    public void setFunctionalGroupCode(String functionalGroupCode) {
        this.functionalGroupCode = functionalGroupCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public void addSystemUser(SystemUser systemUser) {
//        this.sysusers.add(systemUser);
//        systemUser.getFunctionalGroups().add(this);
//    }
//
//    public void removeSystemUser(SystemUser systemUser) {
//        this.sysusers.remove(systemUser);
//        systemUser.getFunctionalGroups().remove(this);
//    }
    public void addSystemModule(Module module) {
        this.modules.add(module);
        module.getFunctionalGroups().add(this);
    }

    public void removeSystemModule(Module module) {
        this.modules.remove(module);
        module.getFunctionalGroups().remove(this);
    }

    @Override
    public String toString() {
        return "FunctionalGroup{" + "functionalGroupId=" + functionalGroupId + ", functionalGroupName=" + functionalGroupName + ", functionalGroupCode=" + functionalGroupCode + ", description=" + description + ", enabled=" + enabled + '}';
    }

}
