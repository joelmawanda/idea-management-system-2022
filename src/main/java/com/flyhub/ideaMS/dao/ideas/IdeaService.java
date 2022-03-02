package com.flyhub.ideaMS.dao.ideas;

import com.flyhub.ideaMS.dao.suggestion.Suggestion;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.models.OperationResponse;
import com.flyhub.ideaMS.utils.ServicesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
public class IdeaService {

    private static final Logger log = Logger.getLogger(IdeaService.class.getName());

    @Autowired
    private ServicesUtils servicesUtils;

    @Autowired
    private IdeaRepository ideaRepository;

    private final String uploadDir = "C:\\Flyhub_projects\\idea-management-system-2022\\src\\main\\resources\\uploads";

    private OperationResponse result;

    public List<Ideas> ListAllIdeas() throws RecordNotFoundException {
        log.info("querying for ideas...");

        List<Ideas> all_ideas = ideaRepository.findAll();

        log.info("system found " + all_ideas.size() + " ideas(s)");

        if (all_ideas == null) {
            throw new RecordNotFoundException(1, String.format("No ideas present"));
        }

        return all_ideas;
    }

    public Ideas updateIdea(String ideaId, Ideas ideas) throws RecordNotFoundException {

        //check that the idea id exists

        Ideas old_idea = ideaRepository.findByIdeaId(ideaId).orElse(null);

        if (old_idea == null) {
            throw new RecordNotFoundException(1, String.format("IdeaID %s does not exist.", ideaId));
        }
        //update the suggestion object by copying the properties from the new object to the old object
        servicesUtils.copyNonNullProperties(ideas, old_idea);

        return ideaRepository.save(old_idea);


    }

    public int deleteIdea(String ideaId) {

        log.info("delete request for ideaId: " + ideaId);

        return ideaRepository.deleteUser(ideaId);

    }

    public Ideas listIdeaByIdeaId(String ideaId) throws RecordNotFoundException {

        log.info("querying for idea by idea Id...");

        Ideas ideas = ideaRepository.findByIdeaId(ideaId).orElse(null);

        log.info("system found " + ideas);

        if (ideas == null) {
            throw new RecordNotFoundException(1, String.format("IdeaId %s not found!", ideaId));
        }

        return ideas;
    }

    public Ideas attachFile(MultipartFile[] files, String ideaDescription, String ideaTitle, String ideaBackground) throws IOException {

        log.info("Uploading  an idea...");

        Ideas ideasAttachment = null;
        Ideas savedVisual = null;

        int i = 0;
        while (i < files.length) {

            ideasAttachment = new Ideas();

            //getting the filename
            String fileName = System.currentTimeMillis() + "-" +  "-idea-attachment";

            //getting directory path uploads/filename
            Path path = Paths.get(uploadDir + fileName);

            //copy file to the directory
            files[i].transferTo(path);
            ideasAttachment.setFilename(fileName);
            ideasAttachment.setIdeaDescription(ideaDescription);
//            ideasAttachment.setCreatedBy(createdBy);
//            ideasAttachment.setModifiedBy(createdBy);
            ideasAttachment.setIdeaTitle(ideaTitle);
            ideasAttachment.setIdeaBackground(ideaBackground);
//            ideasAttachment.setCreateDate(LocalDateTime.now(ZoneId.of("GMT+03:00")));
//            ideasAttachment.setUpdatedOn(LocalDateTime.now(ZoneId.of("GMT+03:00")));
            //ideasAttachment.setIdeaId(ideaId);
            savedVisual = ideaRepository.save(ideasAttachment);
            i++;
        }

        return savedVisual;
    }



//    public OperationResult attachFile(MultipartFile[] files, String description, String createdBy, String ideaId) throws IOException {
//        log.info("Inside attachFile method of Idea Service");
//        Ideas ideasAttachment = null;
//        Ideas savedVisual = null;
//
//        int i = 0;
//        while (i < files.length) {
//            ideasAttachment = new Ideas();
//            //getting the filename
//            String fileName = System.currentTimeMillis() + "-" + ideaId + "-idea-attachment";
//            //getting directory path uploads/filename
//            Path path = Paths.get(uploadDir + fileName);
//            //copy file to the directory
//            files[i].transferTo(path);
//            ideasAttachment.setFilename(fileName);
//            ideasAttachment.setIdeaDescription(description);
//            ideasAttachment.setCreatedBy(createdBy);
//            //ideasAttachment.setModifiedBy(createdBy);
//            //ideasAttachment.setCreateDate(DateTime.now(ZoneId.of("GMT+03:00")));
//            //ideasAttachment.setUpdatedOn(LocalDateTime.now(ZoneId.of("GMT+03:00")));
//            ideasAttachment.setIdeaId(ideaId);
//            savedVisual = ideaRepository.save(ideasAttachment);
//            i++;
//        }
//        if (Objects.nonNull(savedVisual)) {
//            result = new OperationResult(OperationResult.SUCCESS_STATUS, OperationResult.CREATION_SUCCESS_MESSAGE, null);
//        } else {
//            result = new OperationResult(OperationResult.FAILURE_STATUS, OperationResult.OPERATION_FAILURE_MESSAGE, null);
//        }
//        return result;
//    }
}
