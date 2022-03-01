package com.flyhub.ideaMS.dao.functionalgroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FunctionalGroupRepository extends JpaRepository<FunctionalGroup, String> {

    @Query("SELECT f FROM functional_groups f WHERE f.functionalGroupName =:name")
    public Optional<FunctionalGroup> findByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM functional_groups f where f.functionalGroupId=:id")
    public int deleteByFunctionalGroupByIds(@Param("id") String id);

}
