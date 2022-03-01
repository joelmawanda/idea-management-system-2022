package com.flyhub.ideaMS.dao.suggestion;

import com.flyhub.ideaMS.dao.DaoAccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;

@Component
public class SuggestionEntityListener {

    @Autowired
    private DaoAccessUtils daoAccessUtils;

    @PrePersist
    private void beforePersisting(Suggestion suggestion) {
        suggestion.setSuggestionId(daoAccessUtils.generateSuggestionId());
    }

}
