package com.flyhub.ideaMS.dao.country;

import javax.persistence.PrePersist;

/**
 *
 * @author Joel Mawanda
 */
public class CountryEntityListener {

    @PrePersist
    private void beforePersisting(Country country) {
        country.setCountryName(country.getCountryName().toUpperCase());
        country.setCountryCode(country.getCountryCode().toUpperCase());
    }
}
