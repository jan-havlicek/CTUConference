package cz.ctu.ctuconference.attachment.dto;

/**
 * Created by Nick nemame on 14.12.2016.
 */
public class AttachmentDTO {
	String fileName;
	String contentType;
	String downloadUrl;

	public AttachmentDTO(String fileName, String contentType, String downloadUrl) {
		this.fileName = fileName;
		this.contentType = contentType;
		this.downloadUrl = downloadUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
}
