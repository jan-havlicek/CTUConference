package cz.ctu.ctuconference.attachment.domain;

import cz.ctu.ctuconference.conversation.domain.Message;
import cz.ctu.ctuconference.utils.dao.AbstractEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

/**
 * Created by Nick nemame on 04.12.2016.
 */
@Entity
@Table(name="attachment")
public class Attachment extends AbstractEntity {

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "message_id")
	private Message message;

	@Column(name = "content_type")
	private String contentType;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "hash")
	private String hash;

	@Lob
	@Column(name = "file_data", length=1000000)
	private byte[] fileData;

	public Attachment() {
	}

	public Attachment(MultipartFile file) throws IOException {
		this.fileName = file.getOriginalFilename();
		this.contentType = file.getContentType();
		this.fileData = file.getBytes();
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
