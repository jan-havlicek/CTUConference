package cz.ctu.ctuconference.notification.dto;

import java.util.Date;

/**
 * Created by Nick nemame on 29.12.2016.
 */
public abstract class NotificationDTO {
	long id;
	Date dateCreated;
	boolean isRead;

	public NotificationDTO(long id, Date dateCreated, boolean isRead) {
		this.id = id;
		this.dateCreated = dateCreated;
		this.isRead = isRead;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean read) {
		isRead = read;
	}
}
