package com.lbraka.sasebackend.controllers.dto;

/**
 * Created by Lauris on 07/10/2017.
 */
public class ChapterTitle {

    private String chapterNumber;

    private String chapterTitle;

    public ChapterTitle() {
    }

    public ChapterTitle(String chapterNumber, String chapterTitle) {
        this.chapterNumber = chapterNumber;
        this.chapterTitle = chapterTitle;
    }

    public String getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(String chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }
}
