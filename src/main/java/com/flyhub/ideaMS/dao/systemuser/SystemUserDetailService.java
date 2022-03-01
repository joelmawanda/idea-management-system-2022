package com.flyhub.ideaMS.dao.systemuser;

import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMappingService;
import com.flyhub.ideaMS.dao.functionalgroup.FunctionalGroup;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemUserDetailService implements UserDetailsService {

    private static final Logger log = Logger.getLogger(SystemUserDetailService.class.getName());

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private EntityAuthorisationMappingService authorisationMappingService;

    @Override
    public UserDetails loadUserByUsername(String entityId) {

        log.info("loading the principle object...");

        SystemUser user = systemUserService.listSystemUserById(entityId);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("Entity ID %s not Found", entityId));
        }

        List<String> ids = user.getFunctionalGroups()
                .stream()
                .map(FunctionalGroup::getFunctionalGroupId)
                .collect(Collectors.toList());

        //add the userid as well
        ids.add(user.getSystemUserId());

        SystemUserDetails systemUserDetails = new SystemUserDetails(user);
        systemUserDetails.setModuleAuthorities(authorisationMappingService.fetchAllEntityMappingsByEntityIdAsHashMap(ids));

        log.info("loaded the principle object...");

        return systemUserDetails;
    }

}
