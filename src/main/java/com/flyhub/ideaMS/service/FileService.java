package com.flyhub.ideaMS.service;

import com.flyhub.ideaMS.dao.File;
import com.flyhub.ideaMS.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {
  @Autowired
  private FileRepository fileRepository;
  
  public File saveFile(MultipartFile file) {
	  String filename = file.getOriginalFilename();
	  try {
		  File doc = new File(filename,file.getContentType(),file.getBytes());
		  return fileRepository.save(doc);
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
	  return null;
  }
  public Optional<File> getFile(Integer fileId) {
	  return fileRepository.findById(fileId);
  }
  public List<File> getFiles(){
	  return fileRepository.findAll();
  }

//  public void delete(Long attachmentId) {
//		docRepository.deleteById(docId);
//	}
}
