package cz.ctu.ctuconference.notification.domain;

import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.utils.dao.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Nick nemame on 29.12.2016.
 */
@Entity
@Inheritance(strategy= InheritanceType.JOINED)
@DiscriminatorColumn(name="notification_type")
@Table(name="notification")

public class Notification extends AbstractEntity {

	@ManyToOne
	@JoinColumn(name = "receiver_id")
	private AppUser receiver;

	@Basic(optional = false)
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "date_created")
	private Date dateCreated;

	@Basic(optional = false)
	@NotNull
	@Column(name = "is_read")
	private boolean isRead;

	@Column(name = "notification_type", insertable = false, updatable = false)
	private String notificationType;

	public Notification() {
	}

	public Notification(AppUser receiver, Date dateCreated, boolean isRead) {
		this.receiver = receiver;
		this.dateCreated = dateCreated;
		this.isRead = isRead;
	}

	public AppUser getReceiver() {
		return receiver;
	}

	public void setReceiver(AppUser receiver) {
		this.receiver = receiver;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean read) {
		isRead = read;
	}
}
