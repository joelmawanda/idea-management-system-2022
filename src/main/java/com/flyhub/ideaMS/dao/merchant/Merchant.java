package com.flyhub.ideaMS.dao.merchant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.flyhub.ideaMS.User;
import com.flyhub.ideaMS.dao.DaoConstants;
import com.flyhub.ideaMS.dao.Gender;
import com.flyhub.ideaMS.dao.functionalgroup.FunctionalGroup;
import com.flyhub.ideaMS.models.views.View;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Joel Mawanda
 *
 */
@Entity(name = "merchants")
@Table(name = "merchants")
@EntityListeners(MerchantEntityListener.class)
@JsonIgnoreProperties(value = {"user_id", "password", "confirm_password", "functional_groups"}, allowSetters = true)
@JsonPropertyOrder({"merchant_id", "first_name", "last_name", "user_name", "phone_number"})
public class Merchant extends User {

    @Column(name = "merchant_id", unique = true, updatable = false)
    private String merchantId;

    @JsonView(View.SystemAdminView.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = DaoConstants.MERCHANTS_FUNCTIONAL_MAPPINGS_TABLE_NAME,
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "func_id"),
            foreignKey = @ForeignKey(name = "func_id_fk"))
    private final Set<FunctionalGroup> functionalGroups = new HashSet<>();

    @JsonView(View.SystemAdminView.class)
    @Transient
    private final Set<String> functionalGroupNames = new HashSet<>();

    public Merchant() {

    }

    public Merchant(String firstName, String lastName, String username, String password, String email, String phoneNumber) {
        super(firstName, lastName, username, password, email, phoneNumber);

    }

    public Merchant(String firstName, String lastName, String username, String password, String confirmPassword, String email, String phonenumber) {
        super(firstName, lastName, username, password, confirmPassword, email, phonenumber);

    }

    public Merchant(String firstName, String lastName, String prefix, String postfix, String country, Gender gender, String userName, String password, String confirmPassword, String email, String phoneNumber, String dateOfBirth) {
        super(firstName, lastName, prefix, postfix, country, gender, userName, password, confirmPassword, email, phoneNumber, dateOfBirth);
    }

    public Set<FunctionalGroup> getFunctionalGroups() {
        return functionalGroups;
    }

    public void setFunctionalGroups(Set<FunctionalGroup> functionalGroups) {
        this.functionalGroups.clear();
        this.functionalGroups.addAll(functionalGroups);
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Set<String> getFunctionalGroupNames() {
        Set<String> ids = functionalGroups
                .stream()
                .map(FunctionalGroup::getFunctionalGroupName)
                .collect(Collectors.toSet());

        functionalGroupNames.addAll(ids);

        return this.functionalGroupNames;
    }

    public void addFunctionalGroup(FunctionalGroup functionalGroup) {
        this.functionalGroups.add(functionalGroup);
        functionalGroup.getMerchants().add(this);
    }

    public void removeFunctionalGroup(FunctionalGroup functionalGroup) {
        this.functionalGroups.remove(functionalGroup);
        functionalGroup.getMerchants().remove(this);
    }

}
