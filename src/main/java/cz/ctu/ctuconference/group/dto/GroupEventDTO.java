package cz.ctu.ctuconference.group.dto;

import cz.ctu.ctuconference.conversation.dto.ConversationDTO;
import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;

import java.util.Date;

/**
 * Created by Nick Nemame on 02.10.2016.
 */
public class GroupEventDTO extends ConversationEntityDTO {
	private Date dateFrom;
	private Date dateTo;

	public GroupEventDTO(long id, String name, ConversationDTO conversation, Date dateFrom, Date dateTo) {
		super(id, name, conversation);
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		type = "group-event";
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
}
