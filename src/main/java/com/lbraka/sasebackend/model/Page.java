package com.lbraka.sasebackend.model;

import javax.persistence.*;

/**
 * A page of a chapter
 */
@Entity
public class Page {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "page_id")
    private Long id;

    @Column(name = "page_number")
    private Integer pageNumber;

    @Lob
    @Column(name = "page_content")
    private String pageContent;

    @Column(name = "chapter_id")
    private Long chapterId;

    public Page() {
        this.pageContent = "";
    }

    public Page(Long id, Integer pageNumber, String pageContent, Long chapterId) {
        this.id = id;
        this.pageNumber = pageNumber;
        this.pageContent = pageContent;
        this.chapterId = chapterId;
    }

    public Long getId() {
        return id;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public String getPageContent() {
        return pageContent;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }
}
