package cz.ctu.ctuconference.attachment.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick nemame on 24.12.2016.
 */
public class AttachmentUrlListDTO {
	List<String> urlList;

	public AttachmentUrlListDTO(List<String> urlList) {
		this.urlList = urlList;
	}

	public List<String> getUrlList() {
		return urlList;
	}

	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}

	public void addUrl(String url) {
		if(urlList == null) urlList = new ArrayList<>();
		this.urlList.add(url);
	}

}
