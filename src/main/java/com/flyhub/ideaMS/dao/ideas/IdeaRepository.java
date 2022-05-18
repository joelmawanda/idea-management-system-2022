package com.flyhub.ideaMS.dao.ideas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IdeaRepository extends JpaRepository<Ideas, Long> {

    @Query("SELECT i FROM ideas i WHERE i.ideaTitle=:idea")
    public Optional<Ideas> findByIdeaTitle(@Param("idea") String ideaTitle);


    @Modifying
    @Transactional
    @Query("DELETE from ideas i where i.ideaId=:ideaId")
    public int deleteUser(@Param("ideaId") String ideaId);

    @Query("SELECT i FROM ideas i WHERE i.ideaId=:ideaId")
    public Optional<Ideas> findByIdeaId(@Param("ideaId") String ideaId);

    @Query("SELECT f FROM ideas f WHERE f.filename=:file")
    public Optional<Ideas> findByFileName(@Param("file") String file);

}
