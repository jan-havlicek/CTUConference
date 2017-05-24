package cz.ctu.ctuconference.utils.communication;

/**
 * Created by Nick Nemame on 26.09.2016.
 */
public class SocketMessageDTO {
	private String type;
	private Object data = null;

	public SocketMessageDTO(String type, Object data) {
		this.type = type;
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
