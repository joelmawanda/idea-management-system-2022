//package com.flyhub.ideaMS.service;
//
//
//import com.flyhub.ideaMS.entity.User;
//import com.flyhub.ideaMS.exception.DuplicateDataException;
//import com.flyhub.ideaMS.exception.GenericServiceException;
//import com.flyhub.ideaMS.exception.RecordNotFoundException;
//import com.flyhub.ideaMS.repository.UserRepository;
//import org.apache.log4j.Logger;
//import com.flyhub.ideaMS.utils.ServicesUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import java.util.List;
//
//@Service
//public class UserService {
//
//	private static final Logger log = Logger.getLogger(UserService.class.getName());
//
//	@Autowired
//	private ServicesUtils servicesUtils;
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private PasswordEncoder encoder;
//
//	public List<User> ListAllUsers() throws RecordNotFoundException {
//		log.info("querying for users...");
//
//		List<User> all_users_details = userRepository.findAll();
//
//		log.info("system found " + all_users_details.size() + " user(s)");
//
//		if (all_users_details == null) {
//			throw new RecordNotFoundException(1, String.format("No users present"));
//		}
//
//		return all_users_details;
//	}
//
//	public User createUser(User user) throws DuplicateDataException, GenericServiceException {
//		//check if the unique parameters are new
//		//if there is a user that has any of these parameters then
//		// we can not proceed
//		if (userRepository.findByUniqueParameters(user.getUsername(), user.getEmail(), user.getPhonenumber()).isPresent()) {
//			log.info("user data exists...");
//			throw new DuplicateDataException(1, "User Creation can not be completed.Please check duplicate for data.");
//		}
//
//		if (!user.getConfirmPassword().equals(user.getPassword())) {
//			throw new GenericServiceException(1, "Confirm password does not match password");
//		}
//
//		user.setPassword(encoder.encode(user.getPassword()));
//
//		log.info("create a user...");
//
//		return userRepository.save(user);
//
//	}
//
//	public User updateUser(Long userId, User user) throws RecordNotFoundException {
//
//		//check that the user id exists
//		User old_user = userRepository.findById(userId).orElse(null);
//
//		if (old_user == null) {
//			throw new RecordNotFoundException(1, String.format("UserID %s does not exist.", userId));
//		}
//		//update the user object by copying the properties from the new object to the old object
//		servicesUtils.copyNonNullProperties(user, old_user);
//
//		//check if this is an update for password
//		if (user.getPassword() != null) {
//			old_user.setPassword(encoder.encode(user.getPassword()));
//		}
//
//		return userRepository.save(old_user);
//
//
//	}
//
//	public int deleteUser(Long userId) {
//
//		log.info("delete request for userId: " + userId);
//
//		return userRepository.deleteUser(userId);
//
//	}
//
//	public User listUserByUserId(Long userId) throws RecordNotFoundException {
//
//		log.info("querying for user by user Id...");
//
//		User user = userRepository.findByUserId(userId).orElse(null);
//
//		log.info("system found " + user);
//
//		if (user == null) {
//			throw new RecordNotFoundException(1, String.format("UserId %s not found!", userId));
//		}
//
//		return user;
//	}
//
//}
