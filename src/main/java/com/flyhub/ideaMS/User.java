package com.flyhub.ideaMS;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.flyhub.ideaMS.dao.Gender;
import com.flyhub.ideaMS.dao.entityauthority.EntityAuthorisationMappingDto;
import com.flyhub.ideaMS.models.views.View;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Jean Kekirungi
 * @author Joel Mawanda
 *
 */
@Getter
@Setter
@MappedSuperclass
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    
    @NotNull(message = "First name cannot be null")
    private String firstName;

    @Column(name = "last_name")
    
    @NotNull(message = "Last name cannot be null")
    private String lastName;

    @Column(name = "prefix")
    
    private String prefix;

    @Column(name = "postfix")
    
    private String postfix;

    @Column(name = "country")
    @JsonProperty("country_name")
    
    private String country;

    @JsonProperty("gender")
    @NotNull(message = "Please input your gender")
    @Column(name= "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "user_name", nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{2,29}$", message = "Please input correct username(Username must contain between 3 to 30 characters)")
    @NotNull(message = "Username cannot be null")
    private String userName;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password cannot be null")
    private String password;

    @Transient
    private String confirmPassword;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    
    @NotNull(message = "Email cannot be null")
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    @Pattern(regexp = "((07|03|02|08)\\d{8})|(\\+256\\d{9})", message = "Please input correct phone number")
    @NotNull(message = "Phone number cannot be null")
    private String phoneNumber;

    @Column(name = "created_time")
    @CreationTimestamp
    private Date createdTime;

    @Column(name = "last_updated_time")
    private Date lastUpdatedTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "date_of_birth")
    //@PastOrPresent(message = "Date of Birth cannot be in the future")
    @JsonProperty("date_of_birth")
    @Pattern(regexp = "^(?:0[1-9]|[12]\\d|3[01])([\\/.-])(?:0[1-9]|1[012])\\1(?:19|20)\\d\\d$", message = "Please input correct date of birth")
    @NotBlank(message = "Date of birth cannot be null")
    private String dateOfBirth;

    @JsonView(View.SystemAdminView.class)
    @Column(name = "modified_by")
    private String modifiedBy;

    @JsonView(View.SystemAdminView.class)
    @Column(name = "enabled")
    private boolean enabled = true;

    @JsonView(View.SystemAdminView.class)
    @Column(name = "account_non_expired")
    private boolean accountNonExpired = true;

    @JsonView(View.SystemAdminView.class)
    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;

    @JsonView(View.SystemAdminView.class)
    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired = true;

    @JsonView(View.SystemAdminView.class)
    @Column(name = "suspended")
    private boolean suspended;

    @JsonView(View.SystemAdminView.class)
    @Column(name = "under_review")
    private boolean underReview;

    @JsonView(View.SystemAdminView.class)
    @Transient
    @JsonProperty("module_authorities")
    private HashMap<String, EntityAuthorisationMappingDto> authorities;
    
    public User() {
    }

    public User(String firstName, String lastName, String username, String password, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(String firstName, String lastName, String username, String password, String confirmPassword, String email, String phonenumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.phoneNumber = phonenumber;
    }

    public User(String firstName, String lastName, String prefix, String postfix, String country, Gender gender, String userName, String password, String confirmPassword, String email, String phoneNumber, String dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.prefix = prefix;
        this.postfix = postfix;
        this.country = country;
        this.gender = gender;
        this.userName = userName;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }
}
