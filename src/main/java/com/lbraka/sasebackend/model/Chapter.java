package com.lbraka.sasebackend.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Lauris on 14/05/2017.
 */
@Entity
public class Chapter {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "chapter_id")
    private Long id;

    @Column(name = "chapter_title")
    private String title;

    @Column(name = "chapter_number")
    private Integer chapNumber;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "chapterId")
    private List<Page> pages;

    @Column(name = "book_id")
    private Long bookId;

    public Chapter() {
    }

    public Chapter(Long id, String title, Integer chapNumber, Long bookId, List<Page> pages) {
        this.id = id;
        this.title = title;
        this.chapNumber = chapNumber;
        this.bookId = bookId;
        this.pages = pages;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getChapNumber() {
        return chapNumber;
    }

    public Long getBookId() {
        return bookId;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
