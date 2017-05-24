/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.conversation.dto;

import cz.ctu.ctuconference.attachment.dto.AttachmentDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Information about each message of any communication.
 *
 * @author Nick nemame
 */
public class MessageDTO implements Serializable {

    private Long id;

    private String message;

    private Date dateSent;

    private boolean read;

    private long senderId;

	private String senderName;

	private List<AttachmentDTO> attachmentList;

	private String type;

    public MessageDTO(Long id, String message, Date dateSent, boolean read, long senderId, String senderName, String type) {
        this.id = id;
        this.message = message;
        this.dateSent = dateSent;
        this.read = read;
        this.senderId = senderId;
		this.senderName = senderName;
		this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean isRead) {
        this.read = isRead;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<AttachmentDTO> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<AttachmentDTO> attachmentList) {
		this.attachmentList = attachmentList;
	}

	@Override
    public String toString() {
        return "MESSAGE_DTO - id:"+id;
    }

}
