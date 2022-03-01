package com.flyhub.ideaMS.repository;

import java.util.Optional;


import com.flyhub.ideaMS.dao.Ideas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IdeaRepository extends JpaRepository<Ideas, Long> {

    @Query("SELECT i FROM ideas i WHERE i.ideaTitle=:idea")
    public Optional<Ideas> findByIdeaTitle(@Param("idea") String ideaTitle);

    @Modifying
    @Transactional
    @Query("DELETE from ideas i where i.ideaId=:ideaId")
    public int deleteUser(@Param("ideaId") Long ideaId);

    @Query("SELECT i FROM ideas i WHERE i.ideaId=:ideaId")
    public Optional<Ideas> findByIdeaId(@Param("ideaId") Long ideaId);

}
