package com.flyhub.ideaMS.dao.suggestion;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.flyhub.ideaMS.dao.StringPrefixedSequenceIdGenerator;
import com.flyhub.ideaMS.dao.SuggestionType;
import com.flyhub.ideaMS.dao.merchant.MerchantEntityListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "suggestions")
@Table(name = "suggestions")
@EntityListeners(SuggestionEntityListener.class)
@JsonIgnoreProperties(value = {"id"}, allowSetters = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="suggestionId")
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "suggestion_id", unique = true, updatable = false)
    private String suggestionId;

    @JsonProperty("suggestion_title")
    @NotBlank(message = "Suggestion title cannot be null")
    @Column(name = "suggestion_title", length = 2000)
    private String suggestionTitle;

    @JsonProperty("suggestion_description")
    @NotBlank(message = "Suggestion description cannot be null")
    @Column(name = "suggestion_description", length = 64)
    private String suggestionDescription;

    @JsonProperty("suggestion_reason")
    @NotBlank(message = "Suggestion reason cannot be null")
    @Column(name = "suggestion_reason", nullable = false, length = 64)
    private String suggestionReason;

    @Column(name="suggestion_type")
    //@NotBlank(message = "Please input a suggestion type")
    @JsonProperty("suggestion_type")
    @Enumerated(EnumType.STRING)
    private SuggestionType suggestionType;

    @Column(name="viewed_by")
    @JsonProperty("viewed_by")
    private String isViewedBy;

    @Column(name="created_on")
    @JsonProperty("created_on")
    @CreationTimestamp
    private Date createdOn;

}
