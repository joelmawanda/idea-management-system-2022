//package com.flyhub.ideaMS.controller;
//
//import java.util.List;
//import java.util.Optional;
//
//import com.flyhub.ideaMS.entity.User;
//import com.flyhub.ideaMS.exception.DuplicateDataException;
//import com.flyhub.ideaMS.exception.GenericServiceException;
//import com.flyhub.ideaMS.exception.RecordNotFoundException;
//import com.flyhub.ideaMS.models.DataObjectResponse;
//import com.flyhub.ideaMS.models.OperationResult;
//import com.flyhub.ideaMS.security.JwtTokenUtil;
//import com.flyhub.ideaMS.service.UserService;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotNull;
//
//@RestController
//@RequestMapping("api/v1/users")
//public class UserController {
//
//	private static final org.apache.log4j.Logger log = Logger.getLogger(UserController.class.getName());
//
//	@Autowired
//	private UserService userService;
//
//	@Autowired
//	private AuthenticationManager authenticationManager;
//
//	@Autowired
//	private JwtTokenUtil jwtUtil;
//
//	@PostMapping("/request-token")
//	public String generateToken(@RequestBody User authRequest) throws Exception {
//		try {
//			authenticationManager.authenticate(
//					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
//			);
//		} catch (Exception ex) {
//			throw new Exception("inavalid username/password");
//		}
//		return jwtUtil.generateToken(authRequest.getUsername());
//	}
//
//	@PostMapping("/login")
//	public String login(@RequestBody User userlogin) {
//		try {
//			Authentication authenticate = authenticationManager
//					.authenticate(new UsernamePasswordAuthenticationToken(userlogin.getUsername(), userlogin.getPassword()));
//
//			return "Principal Object: " + authenticate;
//
//		} catch (Exception ex) {
//			log.error(ex.getLocalizedMessage(), ex);
//			return ex.getLocalizedMessage();
//		}
//	}
//
//	@GetMapping(path = {"/", ""})
//	public ResponseEntity<?> listUsers() {
//		try {
//			List<User> all_users = userService.ListAllUsers();
//			return new ResponseEntity<>(new DataObjectResponse(0, "Success", all_users), HttpStatus.OK);
//		} catch (RecordNotFoundException ex) {
//			return new ResponseEntity<>(new DataObjectResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
//		}
//	}
//
//	@GetMapping("/{userid}")
//	public ResponseEntity<?> listUserByUserId(@PathVariable("userid") @NotNull(message = "User Id cannot be null") Long userId) {
//		try {
//			User user = userService.listUserByUserId(userId);
//			return new ResponseEntity<>(new DataObjectResponse(0, "Success", user), HttpStatus.OK);
//		} catch (RecordNotFoundException ex) {
//			return new ResponseEntity<>(new DataObjectResponse(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
//		}
//	}
//
//	@PatchMapping("/update")
//	public ResponseEntity<?> updateUser(@RequestParam("userid") @NotNull(message = "UserId cannot be null") Long userId, @RequestBody User user) {
//		try {
//
//			User user1 = userService.updateUser(userId, user);
//
//			return new ResponseEntity<>(new DataObjectResponse(0, "Updated Successfully", user1), HttpStatus.OK);
//
//		} catch (RecordNotFoundException ex) {
//			return new ResponseEntity<>(new OperationResult(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.NOT_FOUND);
//		}
//	}
//
//	@DeleteMapping("/delete")
//	public ResponseEntity<?> deleteUser(@RequestParam("userid") Long userId) {
//
//		int number_of_deleted_rows = userService.deleteUser(userId);
//
//		log.info("delete operation rows count: " + number_of_deleted_rows);
//
//		if (number_of_deleted_rows <= 0) {
//
//			return new ResponseEntity<>(new DataObjectResponse(1, "UserID " + userId + " does not exist."), HttpStatus.NOT_FOUND);
//		} else {
//			return new ResponseEntity<>(new DataObjectResponse(0, "Successfully deleted User with UserID " + userId), HttpStatus.OK);
//		}
//	}
//
//	@PostMapping("user-registration")
//	public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
//		try {
//			User created_user = userService.createUser(user);
//
//			if (created_user != null) {
//				return new ResponseEntity<>(created_user, HttpStatus.CREATED);
//			} else {
//				return new ResponseEntity<>(created_user, HttpStatus.EXPECTATION_FAILED);
//			}
//		} catch (DuplicateDataException ex) {
//			return new ResponseEntity<>(new OperationResult(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
//		} catch (GenericServiceException ex) {
//			return new ResponseEntity<>(new OperationResult(ex.getExceptionCode(), ex.getExceptionMessage()), HttpStatus.EXPECTATION_FAILED);
//		}
//	}
//
//}
