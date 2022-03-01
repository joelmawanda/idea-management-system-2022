package com.flyhub.ideaMS.controller;

import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMapping;
import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMappingService;
import com.flyhub.ideaMS.dao.functionalgroup.FunctionalGroup;
import com.flyhub.ideaMS.dao.functionalgroup.FunctionalGroupService;
import com.flyhub.ideaMS.dao.merchant.Merchant;
import com.flyhub.ideaMS.dao.merchant.MerchantService;
import com.flyhub.ideaMS.dao.module.Module;
import com.flyhub.ideaMS.dao.module.ModuleService;
import com.flyhub.ideaMS.dao.module.SystemModuleNames;
import com.flyhub.ideaMS.dao.systemuser.SystemUserService;
import com.flyhub.ideaMS.exception.DuplicateDataException;
import com.flyhub.ideaMS.exception.GenericServiceException;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.models.DataObjectResponse;
import com.flyhub.ideaMS.models.OperationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Joel Mawanda
 * @author Jean Kekirunga
 */
@RestController
@CrossOrigin(origins = {"${app.api.settings.cross-origin.urls}"})
@RequestMapping("api/v1/appsecurity")
public class AccessControlController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private FunctionalGroupService functionalGroupService;

    @Autowired
    private EntityAuthorisationMappingService entityAuthorisationMappingService;

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].readAllowed")
    @GetMapping("/modules")
    public ResponseEntity<?> listSystemModules() {

        List<Module> modules_list = moduleService.listAllModules();

        return new ResponseEntity<>(new DataObjectResponse(0, modules_list.size() + " Record(s)", modules_list), HttpStatus.OK);
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].updateAllowed")
    @PatchMapping("/addgrouptomerchant")
    public ResponseEntity<?> addFunctionalgroupToMerchant(
            @RequestParam("merchantid") @NotNull(message = "MerchantId cannot be null") String merchantId,
            @RequestParam("groupname") @NotNull(message = "Functional group name cannot be null") String name) {

        try {
            Merchant merchant = merchantService.addFunctionalGroupToMerchant(merchantId, name);

            return new ResponseEntity<>(new DataObjectResponse(0, "Success", merchant), HttpStatus.CREATED);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].updateAllowed")
    @PatchMapping("/removegroupfrommerchant")
    public ResponseEntity<?> removeFunctionalGroupFromMerchant(
            @RequestParam("merchantid") @NotNull(message = "MerchantId cannot be null") String merchantId,
            @RequestParam("groupname") @NotNull(message = "Functional group name cannot be null") String name) {

        try {

            Merchant user = merchantService.removeFunctionalGroupFromMerchant(merchantId, name);

            if (user != null) {
                return new ResponseEntity<>(new DataObjectResponse(0, "Successfully Updated User", user), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new OperationResponse(3, "Failed to remove Functional group from user"), HttpStatus.EXPECTATION_FAILED);
            }

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].updateAllowed")
    @PatchMapping("/addgrouptosystemuser")
    public ResponseEntity<?> addFunctionalgroupToSystemUser(
            @RequestParam("systemid")
            @NotNull(message = "SystemId cannot be null") String systemId,
            @RequestParam("groupname")
            @NotNull(message = "Functional group name cannot be null") String name) {
        try {

            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully added functional group to System User", systemUserService.addFunctionalGroupToSystemUser(systemId, name)), HttpStatus.ACCEPTED);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(3, "Failed to add Functional group to System user"), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].updateAllowed")
    @PatchMapping("/removegroupfromsystemuser")
    public ResponseEntity<?> removeFunctionalGroupFromSystemUser(
            @RequestParam("systemid") @NotNull(message = "SystemID cannot be null") String systemId,
            @RequestParam("groupname") @NotNull(message = "Functional group name cannot be null") String name) {
        try {

            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully removed functional group", systemUserService.removeFunctionalGroupFromSystemUser(systemId, name)), HttpStatus.ACCEPTED);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(3, "Failed to add Functional group to System user"), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].updateAllowed")
    @PatchMapping("/addmoduletogroup")
    public ResponseEntity<?> addSystemModuleToFunctionalgroup(
            @RequestParam("groupname") @NotNull(message = "Functional group name cannot be null") String groupname,
            @RequestParam("modulename") @NotNull(message = "module name cannot be null") String modulename) {
        try {

            return new ResponseEntity<>(new DataObjectResponse(0, "Suscessfully Modified.", functionalGroupService.addModuleToFunctionalGroup(groupname, modulename)), HttpStatus.ACCEPTED);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        } catch (DuplicateDataException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.CONFLICT);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].updateAllowed")
    @PatchMapping("/removemodulefromgroup")
    public ResponseEntity<?> removeModuleFromFunctionalgroup(
            @RequestParam("groupname") @NotNull(message = "Functional group name cannot be null") String groupname,
            @RequestParam("modulename") @NotNull(message = "module name cannot be null") String modulename) {
        try {

            FunctionalGroup functionalGroup = functionalGroupService.removeModuleFromFunctionalGroup(groupname, modulename);

            return new ResponseEntity<>(new DataObjectResponse(0, "module has successfully been removed from the functional group", functionalGroup), HttpStatus.OK);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        } catch (DuplicateDataException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.CONFLICT);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].updateAllowed")
    @PatchMapping("/addmoduletomerchant")
    public ResponseEntity<?> addModuleToMerchant(
            @RequestParam("merchantid") @NotNull(message = "MerchantId cannot be null") String merchantId,
            @RequestParam("modulename") @NotNull(message = "Module Name cannot be null") String modulename) {
        try {

            Merchant merchant = merchantService.addModuleToMerchant(merchantId, modulename);

            return new ResponseEntity<>(new DataObjectResponse(0, "Successful", merchant), HttpStatus.CREATED);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        } catch (DuplicateDataException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].updateAllowed")
    @PatchMapping("/removemodulefrommerchant")
    public ResponseEntity<?> removeModuleFromMerchant(
            @RequestParam("merchantid") @NotNull(message = "MerchantId cannot be null") String merchantId,
            @RequestParam("modulename") @NotNull(message = "Module Name cannot be null") String modulename) {
        try {
            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully Modified", merchantService.removeModuleFromMerchant(merchantId, modulename)), HttpStatus.CREATED);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].updateAllowed")
    @PatchMapping("/addmoduletosystemuser")
    public ResponseEntity<?> addModuleToSystemUser(
            @RequestParam("systemid") @NotNull(message = "systemId cannot be null") String systemId,
            @RequestParam("modulename") @NotNull(message = "Module Name cannot be null") String modulename) {
        try {

            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully Modified", systemUserService.addModuleToSystemUser(systemId, modulename)), HttpStatus.ACCEPTED);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        } catch (DuplicateDataException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.CONFLICT);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].updateAllowed")
    @PatchMapping("/removemodulefromsystemuser")
    public ResponseEntity<?> removeModuleFromSystemUser(
            @RequestParam("systemid") @NotNull(message = "System User Id cannot be null") String systemid,
            @RequestParam("modulename") @NotNull(message = "Module Name cannot be null") String modulename) {
        try {
            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully Modified", systemUserService.removeModuleFromSystemUser(systemid, modulename)), HttpStatus.CREATED);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].updateAllowed")
    @PatchMapping("/modifyentityauthorities")
    public ResponseEntity<?> modifyEntityAuthorities(
            @RequestParam("entityid") @NotBlank(message = "parameter 'entityid' can not be null") String entityid,
            @RequestParam("modulename") @NotBlank(message = "parameter 'modulename' can not be null") String modulename,
            @RequestBody EntityAuthorisationMapping authorisationMapping) {
        try {
            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully Updated.", entityAuthorisationMappingService.modifyEntityAuthorities(entityid, modulename, authorisationMapping)), HttpStatus.CREATED);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
