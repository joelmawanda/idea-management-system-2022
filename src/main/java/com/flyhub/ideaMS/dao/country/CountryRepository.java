package com.flyhub.ideaMS.dao.country;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

    @Query("SELECT c FROM country c WHERE c.countryName =:name")
    public Optional<Country> findByName(@Param("name") String name);

    @Query("SELECT c FROM country c WHERE c.countryCode =:code")
    public Optional<Country> findByCountryCode(@Param("code") String code);

    @Modifying
    @Transactional
    @Query("DELETE FROM country c where c.countryId=:id")
    public int deleteByCountryId(@Param("id") String id);

}
