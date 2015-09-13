package hitwh.yxx.wei.lacusSe.seeker.bean;

import java.util.Date;

/**
 * 封装webpage信息的JavaBean<br>
 * 
 * @author lacus
 *
 */
public class Page implements Comparable {

	/** 标题 */
	private String title;
	/** 关键词 */
	private String keyword;
	/** 描述 */
	private String description;
	/** 地址 */
	private String url;
	/** 获取时间 */
	private Date date;
	/** 整个页面的签名 */
	private String md5;
	/** 整个页面除去无效符号，编码为UF-8的内容 */
	private String utfContent;

	public Page() {

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUtfContent() {
		return utfContent;
	}

	public void setUtfContent(String utfContent) {
		this.utfContent = utfContent;
	}

	@Override
	public String toString() {
		return "\t获取信息： \n title=" + title + ",\n keyword=" + keyword + ",\n description=" + description + ",\n url="
				+ url + ",\n date=" + date + ",\n md5=" + md5;
	}

	@Override
	public int compareTo(Object o) {
		return this.md5.compareTo(((Page) o).getMd5());
	}

}
