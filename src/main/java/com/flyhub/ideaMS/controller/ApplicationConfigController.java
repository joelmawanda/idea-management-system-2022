package com.flyhub.ideaMS.controller;
import com.flyhub.ideaMS.dao.country.Country;
import com.flyhub.ideaMS.dao.country.CountryService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@CrossOrigin(origins = {"${app.api.settings.cross-origin.urls}"})
@RequestMapping("/api/v1/config")
public class ApplicationConfigController {

    @Autowired
    private CountryService countryService;

    //@PreAuthorize("hasRole('" + SystemModuleNames.APP_CONFIG_CONTROLLER + "') && principal.moduleAuthorities['" + SystemModuleNames.APP_CONFIG_CONTROLLER + "'].readAllowed")
    @GetMapping("/country")
    public ResponseEntity<?> listCountries() {
        List<Country> country_list = countryService.listCountries();

        return new ResponseEntity<>(new DataObjectResponse(0, country_list.size() + " Record(s) found.", country_list), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('" + SystemModuleNames.APP_CONFIG_CONTROLLER + "') && principal.moduleAuthorities['" + SystemModuleNames.APP_CONFIG_CONTROLLER + "'].searchAllowed")
    @GetMapping("country/{code}")
    public ResponseEntity<?> listCountryByCountryCode(@PathVariable("code") String code) {
        try {
            return new ResponseEntity<>(new DataObjectResponse(0, "Success!", countryService.listCountryByCountryCode(code)), HttpStatus.OK);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('" + SystemModuleNames.APP_CONFIG_CONTROLLER + "') && principal.moduleAuthorities['" + SystemModuleNames.APP_CONFIG_CONTROLLER + "'].createAllowed")
    @PostMapping("country/create")
    public ResponseEntity<?> createCountry(@Valid @RequestBody Country country) {
        try {
            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully Created Country.", countryService.createACountry(country)), HttpStatus.CREATED);
        } catch (DuplicateDataException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.CONFLICT);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("hasRole('" + SystemModuleNames.APP_CONFIG_CONTROLLER + "') && principal.moduleAuthorities['" + SystemModuleNames.APP_CONFIG_CONTROLLER + "'].updateAllowed")
    @PatchMapping("country/update")
    public ResponseEntity<?> editCountry(@RequestParam("countryid") @NotNull(message = "country id cannot be null") String countryId, @RequestBody Country country) {
        try {
            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully Modified", countryService.updateACountry(countryId, country)), HttpStatus.CREATED);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        } catch (GenericServiceException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PreAuthorize("hasRole('" + SystemModuleNames.APP_CONFIG_CONTROLLER + "') && principal.moduleAuthorities['" + SystemModuleNames.APP_CONFIG_CONTROLLER + "'].deleteAllowed")
    @DeleteMapping("country/delete")
    public ResponseEntity<?> deleteCountry(@RequestParam("countryid") String countryId) {
        try {
            countryService.deleteCountry(countryId);

            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully Deleted Country with ID: " + countryId), HttpStatus.OK);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
