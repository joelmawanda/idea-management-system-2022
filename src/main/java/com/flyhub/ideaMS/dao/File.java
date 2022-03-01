package com.flyhub.ideaMS.dao;

import javax.persistence.*;

@Entity
@Table(name="files")
public class File {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer fileId;
	
	private String fileName;
	private String fileType;
	
	@Lob
	private byte[] data;
	
	public File() {}

	public File(String docName, String docType, byte[] data) {
		super();
		this.fileName = docName;
		this.fileType = docType;
		this.data = data;
	}

	public Integer getDocid() {
		return fileId;
	}

	public void setDocid(Integer docid) {
		this.fileId = docid;
	}

	public String getDocName() {
		return fileName;
	}

	public void setDocName(String docName) {
		this.fileName = docName;
	}

	public String getDocType() {
		return fileType;
	}

	public void setDocType(String docType) {
		this.fileType = docType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
