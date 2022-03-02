package com.flyhub.ideaMS.dao.ideas;

import com.flyhub.ideaMS.dao.suggestion.Suggestion;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.utils.ServicesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
