package com.flyhub.ideaMS.dao.ideas;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.flyhub.ideaMS.dao.Category;
import com.flyhub.ideaMS.dao.Priority;
import com.flyhub.ideaMS.dao.suggestion.SuggestionEntityListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "ideas")
@Table(name = "ideas")
@EntityListeners(IdeasEntityListener.class)
@JsonIgnoreProperties(value = {"id"}, allowSetters = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="ideaId")
public class Ideas {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private Long id;

   @Column(name = "idea_id", unique = true, updatable = false)
   private String ideaId;

   @JsonProperty("idea_title")
   @Column(name = "idea_title")
   private String ideaTitle;

   @JsonProperty("idea_description")
   @Column(name = "idea_description", nullable = false, length = 64)
   private String ideaDescription;

   @JsonProperty("idea_background")
   @Column(name = "idea_background", nullable = false, length = 64)
   private String ideaBackground;

   @Column(name="priority")
   @JsonProperty("priority")
   @Enumerated(EnumType.STRING)
   private Priority priority;

   @Column(name="category")
   //@NotBlank(message = "Please input a category")
   @JsonProperty("category")
   @Enumerated(EnumType.STRING)
   private Category category;

   @Column(name="create_date")
   @JsonProperty("create_date")
   @CreationTimestamp
   private Date createDate;

   @Column(name="created_by")
   @JsonProperty("created_by")
   private String createdBy;

   @Column(name="updated_on")
   @JsonProperty("updated_on")
   @CreationTimestamp
   private Date updatedOn;

   @Column(name="modified_by")
   @JsonProperty("modified_by")
   private String modifiedBy;

//   @Column(name="file", nullable = false)
   @Column(name="file")
   @JsonProperty("file")
   private String filename;


}
	
