package hitwh.yxx.lacusSe.web.pojo;

public class ResultItem {
	
	private String title;
	private String url;
	private String highLight;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "结果: [title=" + title + ", url=" + url + "]      \n";
	}
	public String getHighLight() {
		return highLight;
	}
	public void setHighLight(String highLight) {
		this.highLight = highLight;
	}
	
	

}
