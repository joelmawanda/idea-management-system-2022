package com.flyhub.ideaMS.dao;

import java.sql.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

 	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Entity(name = "ideas")
	@Table(name = "ideas")
 	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="ideaId")
	public class Ideas {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "idea_id")
		private Long ideaId;

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

	}
	
