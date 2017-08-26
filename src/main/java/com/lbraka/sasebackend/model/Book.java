package com.lbraka.sasebackend.model;

import javax.persistence.*;
import java.util.List;

/**
 * A book
 */
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "book_title")
    private String title;

    @Column(name = "book_author")
    private String author;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bookId")
    private List<Chapter> chapters;

    public Book() {
    }

    public Book(Long id, String title, String author, List<Chapter> chapters) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.chapters = chapters;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }
}
