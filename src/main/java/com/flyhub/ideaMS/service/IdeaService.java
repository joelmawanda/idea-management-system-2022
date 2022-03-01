package com.flyhub.ideaMS.service;

import java.util.List;

import com.flyhub.ideaMS.dao.Ideas;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.utils.ServicesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.flyhub.ideaMS.repository.IdeaRepository;

@Service
public class IdeaService {

    private static final Logger log = Logger.getLogger(IdeaService.class.getName());

    @Autowired
    private ServicesUtils servicesUtils;

    @Autowired
    private IdeaRepository ideaRepository;

    public Ideas uploadIdea(Ideas ideas) {

        log.info("Uploading  an idea...");

        return ideaRepository.save(ideas);

    }

    public List<Ideas> ListAllIdeas() throws RecordNotFoundException {
        log.info("querying for ideas...");

        List<Ideas> all_ideas = ideaRepository.findAll();

        log.info("system found " + all_ideas.size() + " idea(s)");

        if (all_ideas == null) {
            throw new RecordNotFoundException(1, String.format("No ideas present"));
        }

        return all_ideas;
    }

    public Ideas updateAnIdea(Long ideaId, Ideas ideas) throws RecordNotFoundException {

        //check that the idea id exists
        Ideas old_idea = ideaRepository.findById(ideaId).orElse(null);

        if (old_idea == null) {
            throw new RecordNotFoundException(1, String.format("IdeaID %s does not exist.", ideaId));
        }
        //update the suggestion object by copying the properties from the new object to the old object
        servicesUtils.copyNonNullProperties(ideas, old_idea);

        return ideaRepository.save(old_idea);


    }

    public int deleteIdea(Long ideaId) {

        log.info("delete request for ideaId: " + ideaId);

        return ideaRepository.deleteUser(ideaId);

    }

    public Ideas listIdeaByIdeaId(Long ideaId) throws RecordNotFoundException {

        log.info("querying for idea by idea Id...");

        Ideas ideas = ideaRepository.findByIdeaId(ideaId).orElse(null);

        log.info("system found " + ideas);

        if (ideas == null) {
            throw new RecordNotFoundException(1, String.format("IdeaId %s not found!", ideaId));
        }

        return ideas;
    }
}
