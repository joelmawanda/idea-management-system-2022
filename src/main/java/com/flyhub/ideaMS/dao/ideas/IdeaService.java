package com.flyhub.ideaMS.dao.ideas;

import com.flyhub.ideaMS.dao.Category;
import com.flyhub.ideaMS.dao.Priority;
import com.flyhub.ideaMS.dao.suggestion.Suggestion;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.models.OperationResponse;
import com.flyhub.ideaMS.utils.ServicesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Scanner;

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

    public Page<Ideas> listIdeasWithPaginationAndSorting(int offset, int pageSize, String field) throws RecordNotFoundException{
        log.info("querying for ideas...");
        Page<Ideas> allideas = ideaRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.DESC, field)));
        log.info("system found " + allideas.getSize() + " ideas(s)");
        if (allideas == null) {
            throw new RecordNotFoundException(1, String.format("No suggestions present"));
        }
        return  allideas;
    }

    public Ideas updateIdea(String ideaId, String modifiedBy, Ideas ideas) throws RecordNotFoundException {

        //check that the idea id exists

        Ideas old_idea = ideaRepository.findByIdeaId(ideaId).orElse(null);

        if (old_idea == null) {
            throw new RecordNotFoundException(1, String.format("IdeaID %s does not exist.", ideaId));
        }
        //update the suggestion object by copying the properties from the new object to the old object
        old_idea.setModifiedBy(modifiedBy);
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

    public Ideas attachFile(MultipartFile[] files, String ideaDescription, String ideaTitle, String ideaBackground, Category category, Priority priority, String createdBy) throws IOException {

        log.info("Uploading  an idea...");

        //Ideas ideasAttachment = null;
        Ideas newIdea = null;

        int i = 0;
        while (i < files.length) {
            System.out.println("This is the file name: " + files[i].getOriginalFilename());

            Ideas ideasAttachment = new Ideas();

            //getting the filename
//            String fileName = System.currentTimeMillis() + "-" +  "-idea-attachment";
            String fileName = System.currentTimeMillis() + "-" +  files[i].getOriginalFilename();


            //getting directory path uploads/filename
            Path path = Paths.get(uploadDir + fileName);

            //copy file to the directory
            files[i].transferTo(path);
            ideasAttachment.setFilename(fileName);
            ideasAttachment.setIdeaDescription(ideaDescription);
            ideasAttachment.setIdeaTitle(ideaTitle);
            ideasAttachment.setIdeaBackground(ideaBackground);
            ideasAttachment.setCategory(category);
            ideasAttachment.setPriority(priority);
            ideasAttachment.setCreatedBy(createdBy);
            newIdea = ideaRepository.save(ideasAttachment);
            i++;
        }

        return newIdea;
    }

    public Ideas downloadFile(String ideaId, String filename, HttpServletResponse response) throws RecordNotFoundException{
        log.info("Downloading an idea...");

        Ideas idea = ideaRepository.findByIdeaId(ideaId).orElse(null);

        if (idea == null) {
            throw new RecordNotFoundException(1, String.format("File does not exist"));
        }
        if (idea.getFilename().indexOf(".doc")>-1) response.setContentType("application/msword");
        //if (idea.getFilename().contains(".docx")) response.setContentType("application/msword");
        if (idea.getFilename().indexOf(".docx")>-1) response.setContentType("application/msword");
        if (idea.getFilename().indexOf(".xls")>-1) response.setContentType("application/vnd.ms-excel");
        if (idea.getFilename().indexOf(".csv")>-1) response.setContentType("application/vnd.ms-excel");
        if (idea.getFilename().indexOf(".ppt")>-1) response.setContentType("application/ppt");
        if (idea.getFilename().indexOf(".pdf")>-1) response.setContentType("application/pdf");
        if (idea.getFilename().indexOf(".zip")>-1) response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" +idea.getFilename());
        response.setHeader("Content-Transfer-Encoding", "binary");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            FileInputStream fis = new FileInputStream(uploadDir+idea.getFilename());
            int len;
            byte[] buf = new byte[1024];
            while((len = fis.read(buf)) > 0) {
                bos.write(buf,0,len);
            }
            bos.close();
            response.flushBuffer();
        }
        catch(IOException e) {
            e.printStackTrace();

        }
        return idea;
    }

    public Ideas readFile(String ideaId) throws RecordNotFoundException {
        log.info("Reading idea file...");

        Ideas idea = ideaRepository.findByIdeaId(ideaId).orElse(null);

        if (idea == null) {
            throw new RecordNotFoundException(1, String.format("File does not exist"));
        }
        try {
            File myObj = new File(uploadDir+idea.getFilename());
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.print("This is the data" + data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return idea;
    }
}



