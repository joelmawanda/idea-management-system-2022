//package com.flyhub.ideaMS.repository;
//
//import java.util.Optional;
//
//import com.flyhub.ideaMS.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//	@Query("SELECT u FROM users u WHERE u.email=:email")
//	public Optional<User> findByEmail(@Param("email") String email);
//
//	@Query("SELECT u FROM users u WHERE u.username=:user")
//	public User findByUsername(@Param("user") String username);
//
//	@Query("SELECT u FROM users u WHERE u.phonenumber=:phone")
//	public Optional<User> findByPhone(@Param("phone") String phoneNumber);
//
//	@Query("select u FROM users u WHERE u.username =:username OR u.email =:email OR u.phonenumber =:phonenumber")
//	public Optional<User> findByUniqueParameters(@Param("username") String username, @Param("email") String email, @Param("phonenumber") String phonenumber);
//
//	@Modifying
//	@Transactional
//	@Query("DELETE from users u where u.userId=:userId")
//	public int deleteUser(@Param("userId") Long userId);
//
//	@Query("SELECT u FROM users u WHERE u.userId=:userId")
//	public Optional<User> findByUserId(@Param("userId") Long userId);
//}
