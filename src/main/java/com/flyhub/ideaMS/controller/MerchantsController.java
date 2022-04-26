package com.flyhub.ideaMS.controller;

import com.flyhub.ideaMS.dao.merchant.Merchant;
import com.flyhub.ideaMS.dao.merchant.MerchantService;
import com.flyhub.ideaMS.dao.module.SystemModuleNames;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.models.DataObjectResponse;
import com.flyhub.ideaMS.models.OperationResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 *
 * @author Joel Mawanda
 *
 */
@RestController
@CrossOrigin(origins = {"${app.api.settings.cross-origin.urls}"})
@RequestMapping("/api/v1/normal-users")
@Validated
public class MerchantsController {

    private static final Logger log = Logger.getLogger(MerchantsController.class.getName());

    @Autowired
    private MerchantService merchantService;

    @PreAuthorize("hasRole('" + SystemModuleNames.APP_SECURITY_CONTROLLER + "') AND principal.moduleAuthorities['" + SystemModuleNames.APP_SECURITY_CONTROLLER + "'].readAllowed")
    @GetMapping(path = {"/", ""})
    public ResponseEntity<?> listMerchants() {

        List<Merchant> all_users_details_with_authorities = merchantService.listAllMerchants();

        return new ResponseEntity<>(new DataObjectResponse(0, all_users_details_with_authorities.size() + " Record (s) found", all_users_details_with_authorities), HttpStatus.OK);
    }

    /**
     *
     * @param merchantId
     * @return
     */
    @PreAuthorize("#merchantId == principal.id OR hasRole('" + SystemModuleNames.APP_SECURITY_CONTROLLER + "')")
    @GetMapping("/{merchantid}")
    public ResponseEntity<?> listMerchantByMerchantId(@PathVariable("merchantid") @NotNull(message = "User Id cannot be null") String merchantId) {
        try {
            Merchant merchant = merchantService.listMerchantByMerchantId(merchantId);
            return new ResponseEntity<>(new DataObjectResponse(0, "Success", merchant), HttpStatus.OK);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new DataObjectResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     *
     * @param merchant
     * @return
     */
    @PreAuthorize("principal.moduleAuthorities['" + SystemModuleNames.MERCHANT_CONTROLLER + "'].searchAllowed")
    @PostMapping("/search")
    public ResponseEntity<?> listMerchantByExample(@RequestBody Merchant merchant) {
        DataObjectResponse dataObjectResponse = merchantService.findMerchantByExampleUsingStringParameters(merchant);

        if (dataObjectResponse.getOperationResult() == 1) {
            return new ResponseEntity<>(dataObjectResponse, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(dataObjectResponse, HttpStatus.OK);
        }
    }

    /**
     * edits a property of the api user class.
     * <em>note: we do not provide the @Valid annotation here</em>
     *
     * @param merchantId query parameter for the merchant id to be modified
     * @param merchant place holder for the property to be modified
     * @return the full object persisted to the database
     */
    @PreAuthorize("(principal.moduleAuthorities['" + SystemModuleNames.MERCHANT_CONTROLLER + "'].updateAllowed AND #merchantId == principal.id) OR hasRole('" + SystemModuleNames.APP_SECURITY_CONTROLLER + "')")
    @PatchMapping("/update")
    public ResponseEntity<?> updateMerchant(@RequestParam("merchantid") @NotNull(message = "User Id cannot be null") String merchantId, @RequestBody Merchant merchant) {
        try {

            Merchant user = merchantService.updateMerchant(merchantId, merchant);

            return new ResponseEntity<>(new DataObjectResponse(0, "Updated Successfully", user), HttpStatus.OK);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     *
     * @param merchantId
     * @return
     */
    @PreAuthorize("hasRole('" + SystemModuleNames.APP_SECURITY_CONTROLLER + "')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMerchant(@RequestParam("merchantid") String merchantId) {

        int number_of_deleted_rows = merchantService.deleteMerchant(merchantId);

        log.info("delete operation rows count: " + number_of_deleted_rows);

        if (number_of_deleted_rows <= 0) {

            return new ResponseEntity<>(new DataObjectResponse(1, "User Id " + merchantId + " does not exist."), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully deleted User with User id " + merchantId), HttpStatus.OK);
        }
    }
}
