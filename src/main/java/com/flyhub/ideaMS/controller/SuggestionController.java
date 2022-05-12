package com.flyhub.ideaMS.controller;

import com.flyhub.ideaMS.dao.merchant.Merchant;
import com.flyhub.ideaMS.dao.suggestion.Suggestion;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.models.DataObjectResponse;
import com.flyhub.ideaMS.models.OperationResponse;
import com.flyhub.ideaMS.dao.suggestion.SuggestionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("api/v1/suggestions")
@CrossOrigin(origins = {"${app.api.settings.cross-origin.urls}"})


public class SuggestionController {

    private static final org.apache.log4j.Logger log = Logger.getLogger(SuggestionController.class.getName());

    @Autowired
    private SuggestionService suggestionService;

    @GetMapping(path = {"/", ""})
    public ResponseEntity<?> listSuggestions() {
        try {
            List<Suggestion> all_suggestions = suggestionService.ListAllSuggestions();
            return new ResponseEntity<>(new DataObjectResponse(0, "Success", all_suggestions), HttpStatus.OK);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new DataObjectResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{offset}/{pageSize}/{field}")
    public ResponseEntity<?> getProductsWithPagination(@PathVariable int offset, @PathVariable int pageSize, @PathVariable String field) {
        try {
            Page<Suggestion> all_suggestions = suggestionService.findProductsWithPaginationAndSorting(offset, pageSize, field);
            return new ResponseEntity<>(new DataObjectResponse(all_suggestions.getSize(), 0, "Success", all_suggestions), HttpStatus.OK);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new DataObjectResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{suggestionid}")
    public ResponseEntity<?> listSuggestionBySuggestionId(@PathVariable("suggestionid") @NotNull(message = "Suggestion Id cannot be null") String suggestionId) {
        try {
            Suggestion suggestion = suggestionService.listSuggestionBySuggestionId(suggestionId);
            return new ResponseEntity<>(new DataObjectResponse(0, "Success", suggestion), HttpStatus.OK);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new DataObjectResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateSuggestion(@RequestParam("suggestionid") @NotNull(message = "SuggestionId cannot be null") String suggestionId, @RequestBody Suggestion suggestion) {
        try {

            Suggestion suggestion1 = suggestionService.updateSuggestion(suggestionId, suggestion);

            return new ResponseEntity<>(new DataObjectResponse(0, "Updated Successfully", suggestion1), HttpStatus.OK);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSuggestion(@RequestParam("suggestionid") String suggestionId) {

        int number_of_deleted_rows = suggestionService.deleteSuggestion(suggestionId);

        log.info("delete operation rows count: " + number_of_deleted_rows);

        if (number_of_deleted_rows <= 0) {

            return new ResponseEntity<>(new DataObjectResponse(1, "SuggestionID " + suggestionId + " does not exist."), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully deleted Suggestion with SuggestionID " + suggestionId), HttpStatus.OK);
        }
    }

    @PostMapping("suggestion-submission")
    public ResponseEntity<?> registerSuggestion(@Valid @RequestBody Suggestion suggestion) {
        Suggestion submitted_suggestion = suggestionService.createSuggestion(suggestion);

        if (submitted_suggestion != null) {
            return new ResponseEntity<>(new DataObjectResponse(0,"Success", submitted_suggestion) , HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(submitted_suggestion, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
