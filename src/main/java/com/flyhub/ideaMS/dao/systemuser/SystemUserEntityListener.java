package com.flyhub.ideaMS.dao.systemuser;

import com.flyhub.ideaMS.dao.DaoAccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.persistence.PrePersist;

@Component
public class SystemUserEntityListener {

    @Autowired
    private DaoAccessUtils daoAccessUtils;

    @PrePersist
    private void beforePersisting(SystemUser systemUser) {
        systemUser.setSystemUserId(daoAccessUtils.generateSystemUserId());
    }

}
