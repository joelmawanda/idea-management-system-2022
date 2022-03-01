//package com.flyhub.ideaMS;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.Date;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
////@SpringBootTest
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = Replace.NONE)
//public class AttachmentRepositoryTests {
//
//	@Autowired
//	private AttachmentRepository attachmentRepository;
//
//	@Autowired
//	private TestEntityManager entityManager;
//
//	@Test
//	public void testInsertAttachment() throws IOException {
//		File file = new File("C:\\Users\\A241902\\documents\\CodeJava.docx");
//
//		Attachment attachment = new Attachment();
//		attachment.setFileName(file.getName());
//
//		byte[] bytes = Files.readAllBytes(file.toPath());
//		attachment.setContent(bytes);
//		long fileSize = bytes.length;
//		attachment.setSize(fileSize);
//		attachment.setUploadTime(new Date());
//
//		Attachment savedDoc = attachmentRepository.save(attachment);
//
//		Attachment existDoc = entityManager.find(Attachment.class, savedDoc.getAttachmentId());
//
//		assertThat(existDoc.getSize()).isEqualTo(fileSize);
//	}
//
//
//}
