//package com.flyhub.ideaMS.entity;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//import com.fasterxml.jackson.databind.PropertyNamingStrategies;
//import com.fasterxml.jackson.databind.annotation.JsonNaming;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.util.Date;
//
//@Entity(name = "users")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//@JsonIgnoreProperties(value = {"password", "confirm_password"}, allowSetters = true)
//@JsonPropertyOrder({"user_id", "first_name", "last_name", "username", "phone_number"})
//@Table(name="users")
//public class User {
//
//	@Id
//	@GeneratedValue(strategy= GenerationType.AUTO)
//	@Column(name= "user_id")
//	private Long userId;
//
//	@JsonProperty("email")
//	@Column(name = "email", nullable = false, unique = true)
//	@NotNull(message = "Email name cannot be null")
//	private String email;
//
//	@JsonProperty("username")
//	@Column(name = "username", nullable = false, unique = true)
//	@NotBlank(message = "Username name cannot be null")
//	private String username;
//
//	@JsonProperty("first_name")
//	@Column(name= "first_name")
//	@NotNull(message = "First name cannot be null")
//	private String firstName;
//
//	@JsonProperty("last_name")
//	@Column(name= "last_name")
//	@NotNull(message = "Last name cannot be null")
//	private String lastName;
//
//	@JsonProperty("password")
//	@NotBlank(message = "Password cannot be null")
//	@Column(name= "password")
//	private String password;
//
//	@Transient
//	@JsonProperty("confirm_password")
//	@NotBlank(message = "Confirm_password cannot be null")
//	@Column(name= "confirm_password")
//	private String confirmPassword;
//
//	@JsonProperty("gender")
//	@Column(name= "gender")
//	//@NotBlank(message = "Please input your gender")
//	@Enumerated(EnumType.STRING)
//	private Gender gender;
//
//	@JsonProperty("status")
//	@Column(name= "status")
//	@Enumerated(EnumType.STRING)
//	private Status status;
//
//	@JsonProperty("phone_number")
//	@Column(name = "phone_number", nullable = false, unique = true)
//	private String phonenumber;
//
//	@Column(name = "country")
//	@JsonProperty("country")
//	@NotNull(message = "Country name cannot be null")
//	private String country;
//
//	@JsonProperty("prefix")
//	@Column(name= "prefix")
//	private String prefix;
//
//	@JsonProperty("postfix")
//	@Column(name= "postfix")
//	private String postfix;
//
//	@Column(name = "created_time")
//	@CreationTimestamp
//	private Date createdTime;
//
//
//}
