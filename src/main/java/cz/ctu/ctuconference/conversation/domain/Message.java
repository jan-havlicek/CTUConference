/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.conversation.domain;

import cz.ctu.ctuconference.attachment.domain.Attachment;
import cz.ctu.ctuconference.utils.dao.AbstractEntity;
import cz.ctu.ctuconference.user.AppUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Nick nemame
 */
@Entity
@Table(name = "message")
public class Message extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private AppUser sender;

    @Basic(optional = false)
    @Column(name = "message")
    private String message;

    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_sent")
    private Date dateSent;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    protected Conversation conversation;

    @Basic(optional = false)
    @Column(name = "is_read")
    private boolean isRead;

	@OneToMany(mappedBy = "message")
	private List<Attachment> attachmentList;

    public Message() {
    }

	public Message(AppUser sender, String message, Date dateSent) {
		this.sender = sender;
		this.message = message;
		this.dateSent = dateSent;
		this.isRead = false;
	}

	public AppUser getSender() {
        return sender;
    }

    public void setSender(AppUser sender) {
        this.sender = sender;
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

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public void addAttachment(Attachment attachment) {
		if(this.attachmentList == null) {
			this.attachmentList = new ArrayList<>();
		}
		this.attachmentList.add(attachment);
	}
}
