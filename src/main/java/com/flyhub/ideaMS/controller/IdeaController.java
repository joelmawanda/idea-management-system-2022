package com.flyhub.ideaMS.controller;

import java.util.List;

import com.flyhub.ideaMS.dao.ideas.IdeaService;
import com.flyhub.ideaMS.dao.ideas.Ideas;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.models.DataObjectResponse;
import com.flyhub.ideaMS.models.OperationResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/v1/ideas")
@EnableAutoConfiguration

public class IdeaController {

    private static final org.apache.log4j.Logger log = Logger.getLogger(IdeaController.class.getName());

    @Autowired
    private IdeaService ideaService;

    @GetMapping(path = {"/", ""})
    public ResponseEntity<?> listIdeas() {
        try {
            List<Ideas> all_ideas = ideaService.ListAllIdeas();
            return new ResponseEntity<>(new DataObjectResponse(0, "Success", all_ideas), HttpStatus.OK);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new DataObjectResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{ideaid}")
    public ResponseEntity<?> listIdeaByIdeaId(@PathVariable("ideaid") @NotNull(message = "Idea Id cannot be null") String ideaId) {
            try {
                Ideas ideas = ideaService.listIdeaByIdeaId(ideaId);
                return new ResponseEntity<>(new DataObjectResponse(0, "Success", ideas), HttpStatus.OK);
            } catch (RecordNotFoundException ex) {
                return new ResponseEntity<>(new DataObjectResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
            }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateIdea(@RequestParam("ideaid") @NotNull(message = "ideaId cannot be null") String ideaId, @RequestBody Ideas ideas) {
        try {

            Ideas ideas1 = ideaService.updateIdea(ideaId, ideas);

            return new ResponseEntity<>(new DataObjectResponse(0, "Updated Successfully", ideas1), HttpStatus.OK);

        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new OperationResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteIdea(@RequestParam("ideaid") String ideaId) {

        int number_of_deleted_rows = ideaService.deleteIdea(ideaId);

        log.info("delete operation rows count: " + number_of_deleted_rows);

        if (number_of_deleted_rows <= 0) {

            return new ResponseEntity<>(new DataObjectResponse(1, "IdeaID " + ideaId + " does not exist."), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new DataObjectResponse(0, "Successfully deleted Idea with IdeaID " + ideaId), HttpStatus.OK);
        }
    }

    @PostMapping("idea-submission")
    public ResponseEntity<?> registerIdea(@Valid @RequestBody Ideas ideas) {
        Ideas submitted_idea = ideaService.uploadIdea(ideas);

        if (submitted_idea != null) {
            return new ResponseEntity<>(submitted_idea, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(submitted_idea, HttpStatus.EXPECTATION_FAILED);
        }
    }

}
