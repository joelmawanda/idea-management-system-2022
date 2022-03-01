package com.flyhub.ideaMS.dao.systemuser;

import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMappingDto;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class SystemUserDetails implements UserDetails, Serializable {

    private static final Logger log = Logger.getLogger(SystemUserDetails.class.getName());

    private final String id;
    private final String password;
    private final String userName;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;
    private final boolean superAdmin;

    private HashMap<String, EntityAuthorisationMappingDto> moduleAuthorities;

    public SystemUserDetails(SystemUser systemUser) {

        this.id = systemUser.getSystemUserId();
        this.password = systemUser.getPassword();
        this.userName = systemUser.getUserName();
        this.isAccountNonExpired = systemUser.isAccountNonExpired();
        this.isAccountNonLocked = systemUser.isAccountNonLocked();
        this.isCredentialsNonExpired = systemUser.isCredentialsNonExpired();
        this.isEnabled = systemUser.isEnabled();
        this.superAdmin = systemUser.isSuperAdmin();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> authorities = moduleAuthorities.keySet()
                .stream()
                .map((String moduleName) -> "ROLE_" + moduleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        log.info("loaded-authorities: " + authorities);

        return authorities;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public String getId() {
        return id;
    }

    public HashMap<String, EntityAuthorisationMappingDto> getModuleAuthorities() {
        return moduleAuthorities;
    }

    public void setModuleAuthorities(HashMap<String, EntityAuthorisationMappingDto> moduleAuthorities) {
        this.moduleAuthorities = moduleAuthorities;
    }

}
