package com.flyhub.ideaMS;

import com.flyhub.ideaMS.configurations.GlobalConfig;
import com.flyhub.ideaMS.dao.country.Country;
import com.flyhub.ideaMS.dao.country.CountryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mawanda Joel
 */
@Slf4j
@Order(3)
@Component
public class DefaultCountryConfiguration implements CommandLineRunner {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public void run(String... args) throws Exception {

        log.debug("checking if there are any Countries");

        if (countryRepository.count() >= 1) {
            log.info("found "+countryRepository.count()+" countries...");
            return;
        }

        log.info("loading default country configuration...");

        for (String country_line : GlobalConfig.COUNTRIES) {
            String[] line = country_line.split("\\,");
            Country country = new Country(line[1], line[0], true);
            countryRepository.save(country);
        }

        log.info("loaded " + GlobalConfig.COUNTRIES.length + " countries...");
        log.info("application ready to receive requests...");

    }
}
