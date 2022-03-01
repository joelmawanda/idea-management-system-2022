package com.flyhub.ideaMS.dao.merchant;

import com.flyhub.ideaMS.dao.DaoAccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.persistence.PrePersist;

@Component
public class MerchantEntityListener {
    @Autowired
    private DaoAccessUtils daoAccessUtils;

    @PrePersist
    private void beforePersisting(Merchant merchant) {
        merchant.setMerchantId(daoAccessUtils.generateMerchantId());
    }

}

