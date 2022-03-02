package com.flyhub.ideaMS.dao.merchant;

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

/**
 *
 * this class represents the principle object of the Merchant
 */
public class MerchantDetails implements UserDetails, Serializable {

    private static final Logger log = Logger.getLogger(MerchantDetails.class.getName());

    private final String id;
    private final String password;
    private final String userName;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean enabled;

    private HashMap<String, EntityAuthorisationMappingDto> moduleAuthorities;

    public MerchantDetails(Merchant merchant) {

        this.id = merchant.getMerchantId();
        this.password = merchant.getPassword();
        this.userName = merchant.getUserName();
        this.isAccountNonExpired = merchant.isAccountNonExpired();
        this.isAccountNonLocked = merchant.isAccountNonLocked();
        this.isCredentialsNonExpired = merchant.isCredentialsNonExpired();
        this.enabled = merchant.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        //List<String> module_names = new ArrayList<>();
//        functionalGroups
//                .stream()
//                .map(functionalGroup -> functionalGroup.getModules())
//                .forEachOrdered(modules -> {
//                    modules.forEach(module -> {
//                        module_names.add(module.getSystemModule().getName());
//                    });
//                });
//        Set<SimpleGrantedAuthority> authorities = module_names
//                .stream()
//                .map((String name) -> "ROLE_" + name)
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toSet());
        Set<SimpleGrantedAuthority> authorities = moduleAuthorities.keySet()
                .stream()
                .map((String moduleName) -> "ROLE_" + moduleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        log.info("loaded-authorities: " + authorities);

        return authorities;

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
        return this.enabled;
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
