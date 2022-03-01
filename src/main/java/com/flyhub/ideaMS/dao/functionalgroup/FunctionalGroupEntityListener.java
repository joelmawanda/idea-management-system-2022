package com.flyhub.ideaMS.dao.functionalgroup;

import javax.persistence.PrePersist;

/**
 *
 * @author Benjamin E Ndugga
 */
public class FunctionalGroupEntityListener {

    @PrePersist
    private void beforePersisting(FunctionalGroup functionalGroup) {
        functionalGroup.setFunctionalGroupName(functionalGroup.getFunctionalGroupName().toUpperCase());
        functionalGroup.setFunctionalGroupCode(functionalGroup.getFunctionalGroupCode().toUpperCase());
    }
}
