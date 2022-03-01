package com.flyhub.ideaMS.dao.module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    @Query("SELECT m FROM modules m WHERE m.systemModule =:systemModule")
    public Optional<Module> findByName(@Param("systemModule") SystemModule systemModule);

    @Query("SELECT m FROM modules m WHERE m.systemModule IN (:systemModules)")
    public List<Module> findByNames(List<SystemModule> systemModules);

    

}
