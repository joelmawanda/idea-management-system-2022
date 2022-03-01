package com.flyhub.ideaMS.controller;

import com.flyhub.ideaMS.dao.module.SystemModuleNames;
import com.flyhub.ideaMS.dao.systemuser.SystemUser;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 *
 * @author Benjamin E Nugga
 */
@RestController
@CrossOrigin(origins = {"${app.api.settings.cross-origin.urls}"})
@Validated
@RequestMapping("/api/v1/sysusers")
public class SystemUsersController {

    @Autowired
    private SystemUserService systemUserService;

    @PreAuthorize("hasRole('" + SystemModuleNames.APP_SECURITY_CONTROLLER + "') && principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].readAllowed")
    @GetMapping(path = {"/", ""})
    public ResponseEntity<?> listSystemUsers() {

        List<SystemUser> all_sys_users_with_authorities = systemUserService.listSystemUsers();

        return new ResponseEntity<>(new DataObjectResponse(0, all_sys_users_with_authorities.size() + " Record(s) found.", all_sys_users_with_authorities), HttpStatus.OK);
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].readAllowed")
    @GetMapping("/{systemId}")
    public ResponseEntity<?> listSystemUserById(@PathVariable("systemId") String systemId) {
        try {

            return new ResponseEntity<>(new DataObjectResponse(0, "Success", systemUserService.listSystemUserByIDWithAuthDetails(systemId)), HttpStatus.OK);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].createAllowed")
    @PostMapping("/create")
    public ResponseEntity<?> createSystemUser(@Valid @RequestBody SystemUser systemUser) {
        try {
            return new ResponseEntity<>(new DataObjectResponse(0, "User created successfully", systemUserService.createSystemUser(systemUser)), HttpStatus.ACCEPTED);
        } catch (DuplicateDataException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("(principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].updateAllowed AND #systemId == principal.id) OR principal.superAdmin")
    @PatchMapping("/update")
    public ResponseEntity<?> editSystemUser(@RequestParam("sysid") @NotNull(message = "SystemId cannot be null") String systemId, @RequestBody SystemUser systemUser) {
        try {

            return new ResponseEntity<>(new DataObjectResponse(0, "Updated System User Successfully", systemUserService.updateSystemUser(systemId, systemUser)), HttpStatus.ACCEPTED);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].deleteAllowed")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSystemUser(@RequestParam("sysid") String systemId) {
        try {
            int num_records_deleted = systemUserService.deleteSystemUser(systemId);

            if (num_records_deleted >= 1) {
                return new ResponseEntity<>(new DataObjectResponse(0, "Successfully deleted System User with Id: " + systemId), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new OperationResponse(1, "No records deleted"), HttpStatus.OK);
            }
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
