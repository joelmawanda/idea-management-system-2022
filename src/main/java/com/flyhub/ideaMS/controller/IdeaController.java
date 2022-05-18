package com.flyhub.ideaMS.controller;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.flyhub.ideaMS.dao.Category;
import com.flyhub.ideaMS.dao.File;
import com.flyhub.ideaMS.dao.Priority;
import com.flyhub.ideaMS.dao.ideas.IdeaService;
import com.flyhub.ideaMS.dao.ideas.Ideas;
import com.flyhub.ideaMS.dao.suggestion.Suggestion;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.models.DataObjectResponse;
import com.flyhub.ideaMS.models.OperationResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/v1/ideas")
@CrossOrigin(origins = {"${app.api.settings.cross-origin.urls}"})
@EnableAutoConfiguration

public class IdeaController {

    private static final org.apache.log4j.Logger log = Logger.getLogger(IdeaController.class.getName());

    @Autowired
    private IdeaService ideaService;

    private final String uploadDir = "C:\\Flyhub_projects\\idea-management-system-2022\\src\\main\\resources\\uploads";

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
    public ResponseEntity<?> updateIdea(@RequestParam("ideaid") @NotNull(message = "ideaId cannot be null") String ideaId, @RequestParam("modified_by") @NotNull(message = "modified_by cannot be null") String modifiedBy, @RequestBody Ideas ideas) {
        try {

            Ideas ideas1 = ideaService.updateIdea(ideaId, modifiedBy, ideas);

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
    public ResponseEntity<?> registerIdea(@RequestParam("file") MultipartFile[] files, @RequestParam("idea_description") @NotBlank String ideaDescription, @RequestParam("idea_background") @NotBlank String ideaBackground, @RequestParam("idea_title") @NotBlank String ideaTitle, @RequestParam("category") @NotBlank Category category, @RequestParam("priority") @NotBlank Priority priority, @RequestParam("created_by") @NotBlank String createdBy) throws IOException {
        Ideas submitted_idea = ideaService.attachFile(files, ideaDescription,ideaTitle, ideaBackground, category, priority, createdBy);
        System.out.println(submitted_idea);

        if (submitted_idea != null) {
            return new ResponseEntity<>(new DataObjectResponse(0,"Success",submitted_idea), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(submitted_idea, HttpStatus.EXPECTATION_FAILED);
        }
    }

//    @GetMapping("/download/{file}")
//    @ResponseBody
//    public void show(@PathVariable("file") String file, HttpServletResponse response) {
//
//        if (file.indexOf(".doc")>-1) response.setContentType("application/msword");
//        if (file.indexOf(".docx")>-1) response.setContentType("application/msword");
//        if (file.indexOf(".xls")>-1) response.setContentType("application/vnd.ms-excel");
//        if (file.indexOf(".csv")>-1) response.setContentType("application/vnd.ms-excel");
//        if (file.indexOf(".ppt")>-1) response.setContentType("application/ppt");
//        if (file.indexOf(".pdf")>-1) response.setContentType("application/pdf");
//        if (file.indexOf(".zip")>-1) response.setContentType("application/zip");
//        response.setHeader("Content-Disposition", "attachment; filename=" +file);
//        response.setHeader("Content-Transfer-Encoding", "binary");
//        try {
//            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
//            FileInputStream fis = new FileInputStream(uploadDir+file);
//            int len;
//            byte[] buf = new byte[1024];
//            while((len = fis.read(buf)) > 0) {
//                bos.write(buf,0,len);
//            }
//            bos.close();
//            response.flushBuffer();
//        }
//        catch(IOException e) {
//            e.printStackTrace();
//
//        }
//    }

    @GetMapping("/download/{file}")
    @ResponseBody
    public ResponseEntity<?> show(@PathVariable("file") @NotNull(message = "file name cannot be null") String file, HttpServletResponse response) {
        try {
            Ideas ideas = ideaService.downloadFile(file, response);
            return new ResponseEntity<>(new DataObjectResponse(0, "Downloaded Successfully", ideas), HttpStatus.OK);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(new DataObjectResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
        }

    }
}
