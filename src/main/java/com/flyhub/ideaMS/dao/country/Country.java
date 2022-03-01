package com.flyhub.ideaMS.dao.country;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.flyhub.ideaMS.dao.StringPrefixedSequenceIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity(name = "country")
@Table(name = "country")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(CountryEntityListener.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Country implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_seq")
    @GenericGenerator(name = "country_seq", strategy = "com.flyhub.ideaMS.dao.StringPrefixedSequenceIdGenerator",
            parameters = {
                @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "50"),
                @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "COUNTRY_"),
                @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%02d")})

    @Column(name = "country_id")
    private String countryId;

    @JsonProperty("country_name")
    @Column(name = "country_name", unique = true)
    @NotBlank(message = "Country name cannot be null")
    private String countryName;

    @Column(name = "country_code", unique = true)
    @NotBlank(message = "Country Code cannot be null")
    private String countryCode;

    @Column(name = "country_active")
    private Boolean countryActive;

//	@OneToMany(mappedBy="country")
//    private List<SystemUser> systemUsers;
//
//	@OneToMany(mappedBy="country")
//	private List<Merchant> merchants;
    public Country(String countryName, String countryCode, Boolean countryActive) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.countryActive = countryActive;
    }
}
