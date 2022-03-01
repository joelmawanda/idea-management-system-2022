package com.flyhub.ideaMS.dao.merchant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jean Kekirungi
 * @author Joel Mawanda
 *
 */
@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    @Query("SELECT u FROM merchants u WHERE u.email=:email")
    public List<Merchant> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM merchants u WHERE u.userName=:user")
    public Merchant findByUsername(@Param("user") String username);

    @Query("SELECT u FROM merchants u WHERE u.phoneNumber=:phonenumber")
    public Merchant findByPhone(@Param("phonenumber") String phoneNumber);

    @Query("SELECT u FROM merchants u WHERE u.firstName=:firstName")
    public Merchant findByName(@Param("firstName") String firstName);

    @Query("SELECT u FROM merchants u WHERE u.merchantId=:merchantId")
    public Optional<Merchant> findByMerchantId(@Param("merchantId") String merchantId);

    @Query("select u from merchants u where u.userName =:entityId OR u.email =:entityId OR u.phoneNumber =:entityId OR u.merchantId =:entityId")
    public Optional<Merchant> findByUniqueId(@Param("entityId") String entityId);

    @Query("select u from merchants u where u.userName =:username OR u.email =:email OR u.phoneNumber =:phonenumber")
    public Optional<Merchant> findByUniqueParameters(@Param("username") String username, @Param("email") String email, @Param("phonenumber") String phonenumber);

    @Modifying
    @Transactional
    @Query("UPDATE merchants u SET u.userName=:username WHERE u.phoneNumber=:phonenumber")
    public int updateUsername(@Param("username") String username, @Param("phonenumber") String phonenumber);

    @Modifying
    @Transactional
    @Query("DELETE from merchants u where u.merchantId=:merchantId")
    public int deleteMerchants(@Param("merchantId") String merchantId);

}
