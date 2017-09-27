package db.entry;

import java.io.Serializable;

/**
 * Created by HanyLuv on 2015-10-23.
 */
public class Book implements Serializable{

    private int id; // book id PK
    /**순번*/
    private String bid;
    /**제목*/
    private String title;
    /**작가*/
    private String author;
    /**출판사*/
    private String company;
    /**출판일*/
    private String published;
    /**국제표준도서번호*/
    private String InternationalBookNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getInternationalBookNumber() {
        return InternationalBookNumber;
    }

    public void setInternationalBookNumber(String internationalBookNumber) {
        InternationalBookNumber = internationalBookNumber;
    }
}
