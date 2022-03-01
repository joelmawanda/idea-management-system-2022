package com.flyhub.ideaMS.dao.country;

import com.flyhub.ideaMS.exception.DuplicateDataException;
import com.flyhub.ideaMS.exception.GenericServiceException;
import com.flyhub.ideaMS.exception.RecordNotFoundException;
import com.flyhub.ideaMS.utils.ServicesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private static final Logger log = Logger.getLogger(CountryService.class.getName());

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ServicesUtils servicesUtils;

    public List<Country> listCountries() {
        log.info("querying for all countries...");

        List<Country> country_list = countryRepository.findAll();

        log.info("found " + country_list.size() + " countries(s)");

        return country_list;

    }

    public Country listCountryByName(String name) throws RecordNotFoundException {

        log.info(String.format("list country by name... (%s)", name));

        Country country = countryRepository.findByName(name).orElse(null);

        if (country == null) {
            throw new RecordNotFoundException(2, String.format("Country with Name: %s not Found.", name));
        }

        log.info(String.format("Found country details: %s", country));

        return country;
    }

    public Country listCountryByCountryCode(String code) throws RecordNotFoundException {

        log.info("querying for country Code details for Code: " + code);

        Country country = countryRepository.findByCountryCode(code)
                .orElse(null);

        if (countryRepository == null) {
            throw new RecordNotFoundException(2, String.format("Country with code: %s not Found.", code));
        }

        log.info("country details: " + country);

        return country;

    }

    public Country createACountry(Country country) throws DuplicateDataException, GenericServiceException {

        log.info("creating country...");

        if (countryRepository.findByName(country.getCountryName().toUpperCase()).isPresent()) {
            throw new DuplicateDataException(2, "Country Name: " + country.getCountryName() + " already exists.");
        }

        Country created_country = countryRepository.save(country);

        log.info("created gender: " + created_country);

        if (created_country != null) {
            return created_country;
        } else {
            throw new GenericServiceException(4, "Failed to create Country with name: " + country.getCountryName());
        }
    }

    public Country updateACountry(String id, Country country) throws RecordNotFoundException, GenericServiceException {

        log.info("updating gender...");

        //check if the gender exists in the database
        Country old_country = countryRepository.findById(id).orElse(null);

        if (old_country == null) {

            throw new RecordNotFoundException(1, "Country Id " + id + " does not exist!");

        }

        servicesUtils.copyNonNullProperties(country, old_country);

        Country edited_country = countryRepository.save(old_country);

        log.info("updated country: " + edited_country);

        if (edited_country == null) {

            throw new GenericServiceException(3, "Failed to create Country.");

        } else {

            return edited_country;

        }

    }

    public int deleteCountry(String id) throws RecordNotFoundException {

        log.info("delete request for country id: " + id);

        int number_of_deleted_rows = countryRepository.deleteByCountryId(id);

        log.info("delete operation rows count: " + number_of_deleted_rows);

        if (number_of_deleted_rows <= 0) {

            throw new RecordNotFoundException(1, "Country Id  " + id + " does not exist.");

        }

        return number_of_deleted_rows;
    }

}
