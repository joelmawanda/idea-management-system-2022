package com.flyhub.ideaMS.dao.systemuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

    @Query("SELECT s FROM sys_users s WHERE s.userName=:user")
    public Optional<SystemUser> findByUsername(@Param("user") String username);

    @Query("SELECT s FROM sys_users s WHERE s.systemUserId=:systemId")
    public Optional<SystemUser> findBySystemId(@Param("systemId") String systemId);

    @Query("SELECT s FROM sys_users s WHERE s.systemUserId =:entityId OR s.userName =:entityId OR s.email =:entityId OR s.phoneNumber =:entityId")
    public Optional<SystemUser> findByUniqueId(@Param("entityId") String entityId);

    @Query("select s from sys_users s where s.userName =:username OR s.email =:email OR s.phoneNumber =:phonenumber")
    public Optional<SystemUser> findByUniqueParameters(@Param("username") String username, @Param("email") String email, @Param("phonenumber") String phonenumber);

    @Modifying
    @Transactional
    @Query("DELETE FROM sys_users s WHERE s.systemUserId=:systemId")
    public int deleteSystemUsers(@Param("systemId") String systemId);

}
