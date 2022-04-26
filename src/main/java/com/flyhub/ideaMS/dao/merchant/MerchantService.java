package com.flyhub.ideaMS.dao.merchant;

import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMapping;
import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMappingService;
import com.flyhub.ideaMS.dao.functionalgroup.FunctionalGroup;
import com.flyhub.ideaMS.dao.functionalgroup.FunctionalGroupService;
import com.flyhub.ideaMS.dao.module.Module;
import com.flyhub.ideaMS.dao.module.ModuleService;
import com.flyhub.ideaMS.exception.DuplicateDataException;
import com.flyhub.ideaMS.exception.GenericServiceException;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.models.DataObjectResponse;
import com.flyhub.ideaMS.utils.ServicesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationType.INDIVIDUAL;


@Service
public class MerchantService {

    private static final Logger log = Logger.getLogger(MerchantService.class.getName());

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private FunctionalGroupService functionalGroupService;

    @Autowired
    private EntityAuthorisationMappingService entityAuthorisationMappingService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ServicesUtils servicesUtils;

    public List<Merchant> listAllMerchants() {
        log.info("querying for all api users...");

        List<Merchant> all_users_details = merchantRepository.findAll();

        log.info("system found " + all_users_details.size() + " api user(s)");

        List<Merchant> all_users_details_with_authorities = all_users_details
                .stream()
                .map((Merchant merchant) -> {
                    merchant.setAuthorities(entityAuthorisationMappingService.fetchAllEntityMappingsByEntityIdAsHashMap(merchant.getMerchantId()));
                    return merchant;
                }).collect(Collectors.toList());

        return all_users_details_with_authorities;

    }

    public boolean merchantExists(String merchantId) {

        return merchantRepository.findByMerchantId(merchantId).isPresent();
    }

    public Merchant listMerchantByMerchantId(String merchantId) throws RecordNotFoundException {

        log.info("querying for api user by merchant Id...");

        Merchant merchant = merchantRepository.findByMerchantId(merchantId).orElse(null);

        log.info("system found " + merchant);

        if (merchant == null) {
            throw new RecordNotFoundException(1, String.format("MerchantId %s not found!", merchantId));
        }

        merchant.setAuthorities(entityAuthorisationMappingService.fetchAllEntityMappingsByEntityIdAsHashMap(merchant.getMerchantId()));

        return merchant;
    }

    public Merchant createMerchant(Merchant merchant) throws DuplicateDataException, GenericServiceException {
        //check if the unique parameters are new
        //if there is a user that has any of these parameters then
        // we can not proceed
        if (merchantRepository.findByUniqueParameters(merchant.getUserName(), merchant.getEmail(), merchant.getPhoneNumber()).isPresent()) {
            log.info("user data exists...");
            throw new DuplicateDataException(1, "User Creation can not be completed.Please check duplicate for data.");
        }

        if (!merchant.getConfirmPassword().equals(merchant.getPassword())) {
            throw new GenericServiceException(1, "Confirm password does not match password");
        }

        merchant.setPassword(encoder.encode(merchant.getPassword()));

        log.info("creating user...");

        return merchantRepository.save(merchant);

    }

    public Merchant updateMerchant(String merchantId, Merchant merchant) throws RecordNotFoundException {

        //check that the merchant id exists
        Merchant old_merchant_user = merchantRepository.findByMerchantId(merchantId).orElse(null);

        if (old_merchant_user == null) {
            throw new RecordNotFoundException(1, String.format("MerchantID %s does not exist.", merchantId));
        }

        //set the currently set functional groups
        merchant.setFunctionalGroups(old_merchant_user.getFunctionalGroups());

        //update the user object by copying the properties from the new object to the old object
        servicesUtils.copyNonNullProperties(merchant, old_merchant_user);

        //check if this is an update for password
        if (merchant.getPassword() != null) {
            old_merchant_user.setPassword(encoder.encode(merchant.getPassword()));
        }

        return merchantRepository.save(old_merchant_user);

    }

    public int deleteMerchant(String merchantId) {

        log.info("delete request for MerhantId: " + merchantId);

        return merchantRepository.deleteMerchants(merchantId);

    }

    public Merchant addFunctionalGroupToMerchant(String merchantId, String name) throws RecordNotFoundException, DuplicateDataException, GenericServiceException {

        log.info("assigning merchant id :" + merchantId + " to group: " + name);

        Merchant merchant = merchantRepository.findByMerchantId(merchantId).orElse(null);

        if (merchant == null) {

            throw new RecordNotFoundException(1, String.format("MerchantId %s not found!", merchantId));
        }

        FunctionalGroup functionalGroup = functionalGroupService.listFunctionalGroupByName(name);

        if (functionalGroup == null) {
            throw new RecordNotFoundException(1, String.format("Functional Group %s does not exist.", name));
        }

        Set<FunctionalGroup> functionalGroups = merchant.getFunctionalGroups();

        if (functionalGroups.contains(functionalGroup)) {
            throw new DuplicateDataException(2, "Functional group already exists");
        }

        functionalGroups.add(functionalGroup);

        Merchant new_user = merchantRepository.save(merchant);
        if (new_user == null) {
            throw new GenericServiceException(3, "Failed to add Functional group to user");
        }
        return listMerchantByMerchantId(merchantId);

    }

    public Merchant removeFunctionalGroupFromMerchant(String merchantId, String name) throws RecordNotFoundException {

        Merchant merchant = merchantRepository.findByMerchantId(merchantId).orElse(null);

        if (merchant == null) {
            throw new RecordNotFoundException(1, String.format("MerchantId %s not found!", merchantId));
        }

        FunctionalGroup functionalGroup = functionalGroupService.listFunctionalGroupByName(name);

        if (functionalGroup == null) {
            throw new RecordNotFoundException(1, String.format("FunctionalGroup %s does not exist.", name));
        }

        Set<FunctionalGroup> functionalGroups = merchant.getFunctionalGroups();

        if (!functionalGroups.contains(functionalGroup)) {
            throw new RecordNotFoundException(1, String.format("FunctionalGroup %s is not assigned to user.", name));
        }

        functionalGroups.remove(functionalGroup);

        return merchantRepository.save(merchant);

    }

    public Merchant addModuleToMerchant(String merchantId, String modulename) throws RecordNotFoundException, DuplicateDataException {
        try {

            //check if this merchant id exists
            if (!merchantRepository.findByMerchantId(merchantId).isPresent()) {
                throw new RecordNotFoundException(1, String.format("Merchant Id %s does not exist", merchantId));
            }

            //check if this module name exists, this should throw and example
            Module module = moduleService.resolveSystemModuleName(modulename);

            //check if this mapping is available
            if (entityAuthorisationMappingService.mappingExists(merchantId, modulename)) {
                throw new DuplicateDataException(1, String.format("Mapping between MerchantID %s and Module %s already exists.", merchantId, modulename));
            }

            //add entry into the entity authorities table
            EntityAuthorisationMapping eam = new EntityAuthorisationMapping();
            eam.setEntityId(merchantId);
            eam.setEntityType(INDIVIDUAL);
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

            return listMerchantByMerchantId(merchantId);

        } catch (IllegalArgumentException ex) {
            throw new RecordNotFoundException(1, String.format("Unknown Module with Name: %s", modulename));
        }
    }

    public Merchant removeModuleFromMerchant(String merchantId, String modulename) throws RecordNotFoundException, GenericServiceException {
        try {

            //check if this merchant id exists
            if (!merchantRepository.findByMerchantId(merchantId).isPresent()) {
                throw new RecordNotFoundException(1, String.format("Merchant Id %s does not exist", merchantId));
            }

            //check if this module name exists, this should throw and example
            Module module = moduleService.resolveSystemModuleName(modulename);

            //check if this mapping is available
            if (!entityAuthorisationMappingService.mappingExists(merchantId, modulename)) {
                throw new RecordNotFoundException(1, String.format("Mapping between MerchantID %s and Module %s doesn't exist.", merchantId, modulename));
            }

            Integer rows_deleted = entityAuthorisationMappingService.removeModuleFromEntity(merchantId, module.getModuleId());

            if (rows_deleted < 1) {
                throw new GenericServiceException(1, String.format("Failed to delete mapping better MerchantID %s and Modle %s", merchantId, modulename));
            }

            return listMerchantByMerchantId(merchantId);

        } catch (IllegalArgumentException ex) {

            throw new RecordNotFoundException(1, String.format("Unknown Module with Name: %s", modulename));

        }
    }

    /**
     * fetches data based on all string based properties, the record must match
     *
     * @param merchantProbe
     * @return
     */
    public DataObjectResponse findMerchantByExampleUsingStringParameters(Merchant merchantProbe) {
        DataObjectResponse operationResponse;
        log.info("querying for all api users using QBE string...");

        String[] all_string_based_property_names_with_nulls = servicesUtils.getAllStringBasedPropertyNamesWithNullValues(merchantProbe);

        ExampleMatcher merchantMatcher = ExampleMatcher
                .matchingAny()
                .withIgnorePaths(all_string_based_property_names_with_nulls)
                .withIgnoreCase()
                //.withIgnoreCase(all_string_based_property_names)
                //We have two options INCLUDE, IGNORE. If we set NullHandler to INCLUDE, null fields will be added in where statement asis null condition.
                .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                //indicates how string fields should be matched. DEFAULT/EXACT/STARTING/ENDING/CONTAINING/REGEX.
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);

        List<Merchant> all_api_users_details = merchantRepository.findAll(Example.of(merchantProbe, merchantMatcher));

        log.info("system found " + all_api_users_details.size() + " api user(s)");

        if (all_api_users_details.size() <= 0) {
            operationResponse = new DataObjectResponse(1, "No Matching Record Found ", all_api_users_details);

        } else {
            operationResponse = new DataObjectResponse(0, all_api_users_details.size() + " Record(s) found.", all_api_users_details);
        }

        return operationResponse;
    }

}
