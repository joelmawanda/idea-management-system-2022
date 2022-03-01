package com.flyhub.ideaMS.dao.suggestion;

import com.flyhub.ideaMS.dao.suggestion.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

    @Query("SELECT s FROM suggestions s WHERE s.suggestionTitle=:suggestionTitle")
    public Optional<Suggestion> findByTitle(@Param("suggestionTitle") String suggestionTitle);

    @Modifying
    @Transactional
    @Query("DELETE from suggestions s where s.suggestionId=:suggestionId")
    public int deleteUser(@Param("suggestionId") Long suggestionId);

    @Query("SELECT s FROM suggestions s WHERE s.suggestionId=:suggestionId")
    public Optional<Suggestion> findBySuggestionId(@Param("suggestionId") Long suggestionId);

}
