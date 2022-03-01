package com.flyhub.ideaMS.controller;

import com.flyhub.ideaMS.dao.functionalgroup.FunctionalGroup;
import com.flyhub.ideaMS.dao.functionalgroup.FunctionalGroupService;
import com.flyhub.ideaMS.dao.module.SystemModuleNames;
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
 * @author Benjamin E Ndugga
 */

@RestController
@CrossOrigin(origins = {"${app.api.settings.cross-origin.urls}"})
@RequestMapping("api/v1/functionalgroups")
@Validated
public class FunctionalGroupsController {

    @Autowired
    private FunctionalGroupService functionalGroupService;

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].readAllowed")
    @GetMapping(path = {"/", ""})
    public ResponseEntity<?> listFunctionalGroups() {
        List<FunctionalGroup> functional_groups_list_with_authorities = functionalGroupService.listFunctionalGroups();

        return new ResponseEntity<>(new DataObjectResponse(0, functional_groups_list_with_authorities.size() + " Record(s) found.", functional_groups_list_with_authorities), HttpStatus.OK);
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].searchAllowed")
    @GetMapping("/{id}")
    public ResponseEntity<?> listFunctionalGroupById(@PathVariable("id") String id) {
        try {
            return new ResponseEntity<>(new DataObjectResponse(0, "Success!", functionalGroupService.listFunctionalGroupById(id)), HttpStatus.OK);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].createAllowed")
    @PostMapping("/create")
    public ResponseEntity<?> createFunctionalGroup(@Valid @RequestBody FunctionalGroup functionalGroup) {
        try {
            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully Created Functional Group.", functionalGroupService.createFunctionalGroup(functionalGroup)), HttpStatus.CREATED);
        } catch (DuplicateDataException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.CONFLICT);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].updateAllowed")
    @PatchMapping("/update")
    public ResponseEntity<?> editFunctionalGroup(@RequestParam("groupid") @NotNull(message = "functional group id cannot be null") String functionalGroupId, @RequestBody FunctionalGroup functionalGroup) {
        try {
            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully Modified", functionalGroupService.updateFunctionalGroup(functionalGroupId, functionalGroup)), HttpStatus.CREATED);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].deleteAllowed")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFunctionalGroup(@RequestParam("groupid") String functionalGroupId) {
        try {
            functionalGroupService.deleteFunctionalGroup(functionalGroupId);

            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully Deleted Functional Group with ID: " + functionalGroupId), HttpStatus.OK);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
