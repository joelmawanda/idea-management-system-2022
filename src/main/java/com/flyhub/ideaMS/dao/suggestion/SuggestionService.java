package com.flyhub.ideaMS.dao.suggestion;

import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.utils.ServicesUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class SuggestionService {

    private static final Logger log = Logger.getLogger(SuggestionService.class.getName());

    @Autowired
    private ServicesUtils servicesUtils;

    @Autowired
    private SuggestionRepository suggestionRepository;

    public List<Suggestion> ListAllSuggestions() throws RecordNotFoundException {
        log.info("querying for suggestions...");

        List<Suggestion> all_suggestions = suggestionRepository.findAll();

        log.info("system found " + all_suggestions.size() + " suggestion(s)");

        if (all_suggestions == null) {
            throw new RecordNotFoundException(1, String.format("No suggestions present"));
        }

        return all_suggestions;
    }

    public Suggestion createSuggestion(Suggestion suggestion) {

        log.info("Uploading a suggestion...");

        return suggestionRepository.save(suggestion);

    }

    public Suggestion updateSuggestion(Long suggestionId, Suggestion suggestion) throws RecordNotFoundException {

        //check that the suggestion id exists
        Suggestion old_suggestion = suggestionRepository.findById(suggestionId).orElse(null);

        if (old_suggestion == null) {
            throw new RecordNotFoundException(1, String.format("SuggestionID %s does not exist.", suggestionId));
        }
        //update the suggestion object by copying the properties from the new object to the old object
        servicesUtils.copyNonNullProperties(suggestion, old_suggestion);

        return suggestionRepository.save(old_suggestion);


    }

    public int deleteSuggestion(Long suggestionId) {

        log.info("delete request for suggestionId: " + suggestionId);

        return suggestionRepository.deleteUser(suggestionId);

    }

    public Suggestion listSuggestionBySuggestionId(Long suggestionId) throws RecordNotFoundException {

        log.info("querying for suggestion by suggestion Id...");

        Suggestion suggestion = suggestionRepository.findBySuggestionId(suggestionId).orElse(null);

        log.info("system found " + suggestion);

        if (suggestion == null) {
            throw new RecordNotFoundException(1, String.format("SuggestionId %s not found!", suggestionId));
        }

        return suggestion;
    }
}