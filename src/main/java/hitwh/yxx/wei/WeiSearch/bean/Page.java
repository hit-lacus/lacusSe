package hitwh.yxx.wei.WeiSearch.bean;

import java.util.Date;

public class Page {

    private String title;
    private String keyword;
    private String description;
    private String url;

    private Date date;
    private String md5;
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
        return "\t获取信息： \n title=" + title + ",\n keyword=" + keyword + ",\n description=" + description + ",\n url=" + url
                + ",\n date=" + date + ",\n md5=" + md5;
    }

}
