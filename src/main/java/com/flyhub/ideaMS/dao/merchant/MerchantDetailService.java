package com.flyhub.ideaMS.dao.merchant;

import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMappingService;
import com.flyhub.ideaMS.dao.functionalgroup.FunctionalGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MerchantDetailService implements UserDetailsService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private EntityAuthorisationMappingService entityAuthorisationMappingService;

    @Override
    public UserDetails loadUserByUsername(String entitId) {

        Merchant merchant = merchantRepository.findByUniqueId(entitId).orElse(null);

        if (merchant == null) {
            throw new UsernameNotFoundException(String.format("No user found with entity ID %s not Found.", entitId));
        }

        List<String> ids = merchant.getFunctionalGroups()
                .stream()
                .map(FunctionalGroup::getFunctionalGroupId)
                .collect(Collectors.toList());

        ids.add(merchant.getMerchantId());

        MerchantDetails merchantDetails = new MerchantDetails(merchant);
        merchantDetails.setModuleAuthorities(entityAuthorisationMappingService.fetchAllEntityMappingsByEntityIdAsHashMap(ids));

        return merchantDetails;
    }

}
