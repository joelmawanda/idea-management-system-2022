package com.flyhub.ideaMS.dao.ideas;

import com.flyhub.ideaMS.dao.DaoAccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;

@Component
public class IdeasEntityListener {

    @Autowired
    private DaoAccessUtils daoAccessUtils;

    @PrePersist
    private void beforePersisting(Ideas ideas) {

        ideas.setIdeaId(daoAccessUtils.generateIdeasId());
    }

}
