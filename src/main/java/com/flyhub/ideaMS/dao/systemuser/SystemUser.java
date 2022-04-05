package com.flyhub.ideaMS.dao.systemuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.flyhub.ideaMS.User;
import com.flyhub.ideaMS.dao.DaoConstants;
import com.flyhub.ideaMS.dao.Gender;
import com.flyhub.ideaMS.dao.country.Country;
import com.flyhub.ideaMS.dao.functionalgroup.FunctionalGroup;
import com.flyhub.ideaMS.models.views.View;
import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "sys_users")
@Table(name = "sys_users")
@EntityListeners(SystemUserEntityListener.class)
@JsonIgnoreProperties(value = {"user_id", "functional_groups", "password", "confirm_password"}, allowSetters = true)
@JsonPropertyOrder({"systemUserId", "first_name", "last_name", "user_name", "phone_number"})
public class SystemUser extends User {

    @Column(name = "system_user_id")
    private String systemUserId;

    @JsonView(View.SystemAdminView.class)
    @Column(name = "super_admin")
    private boolean superAdmin = false;

    @JsonView(View.SystemAdminView.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = DaoConstants.SYS_USER_FUNCTIONAL_MAPPINGS_TABLE_NAME,
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "func_id"),
            foreignKey = @ForeignKey(name = "func_id_fk"))
    private Set<FunctionalGroup> functionalGroups = new HashSet<>();

    @JsonView(View.SystemAdminView.class)
    @Transient
    private final Set<String> functionalGroupNames = new HashSet<>();

    public SystemUser(String firstName, String lastName, String username, String password, String email, String phoneNumber) {
        super(firstName, lastName, username, password, email, phoneNumber);
    }

    public SystemUser(String firstName, String lastName, String username, String password, String confirmPassword, String email, String phonenumber, boolean superAdmin) {
        super(firstName, lastName, username, password, confirmPassword, email, phonenumber);
        this.superAdmin = superAdmin;
    }

    public SystemUser(String firstName, String lastName, String prefix, String postfix, String country, Gender gender, String userName, String password, String confirmPassword, String email, String phoneNumber, String dateOfBirth, boolean superAdmin) {
        super(firstName, lastName, prefix, postfix, country, gender, userName, password, confirmPassword, email, phoneNumber, dateOfBirth);
        this.superAdmin = superAdmin;
    }

    public Set<FunctionalGroup> getFunctionalGroups() {
        return functionalGroups;
    }

    public void setFunctionalGroups(Set<FunctionalGroup> functionalGroups) {
        this.functionalGroups.clear();
        this.functionalGroups.addAll(functionalGroups);
    }

    public Set<String> getFunctionalGroupNames() {
        Set<String> functional_group_names = functionalGroups
                .stream()
                .map(FunctionalGroup::getFunctionalGroupName)
                .collect(Collectors.toSet());

        functionalGroupNames.addAll(functional_group_names);

        return this.functionalGroupNames;
    }

    public void addFunctionalGroup(FunctionalGroup functionalGroup) {
        this.functionalGroups.add(functionalGroup);
        functionalGroup.getSysusers().add(this);
    }

    public void removeFunctionalGroup(FunctionalGroup functionalGroup) {
        this.functionalGroups.remove(functionalGroup);
        functionalGroup.getSysusers().remove(this);
    }
}
