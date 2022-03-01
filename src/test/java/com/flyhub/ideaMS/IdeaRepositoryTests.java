package com.flyhub.ideaMS;

import static org.assertj.core.api.Assertions.assertThat;

import com.flyhub.ideaMS.dao.Ideas;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import com.flyhub.ideaMS.repository.IdeaRepository;

@DataJpaTest 
//@Rollback(false)

public class IdeaRepositoryTests {
	
	@Autowired
	private IdeaRepository ideaRepository;
	
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateIdea() {
		Ideas idea = new Ideas();
		idea.setIdeaTitle("Mobile Parking App");
		idea.setIdeaDescription("It is an Android Mobile Parking App");
		idea.setIdeaBackground("Driver's Parking");

		Ideas savedIdea = ideaRepository.save(idea);
		Ideas existIdea = entityManager.find(Ideas.class, savedIdea.getIdeaId());
		assertThat(existIdea.getIdeaTitle()).isEqualTo(idea.getIdeaTitle());
	}
	

}
