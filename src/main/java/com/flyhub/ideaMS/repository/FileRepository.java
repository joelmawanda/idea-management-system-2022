package com.flyhub.ideaMS.repository;

import com.flyhub.ideaMS.dao.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File,Integer> {

}
