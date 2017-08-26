package com.lbraka.sasebackend.controllers.dto;

import com.lbraka.sasebackend.model.Chapter;

/**
 * Created by Lauris on 22/05/2017.
 */
public class ChapterContent {

    private Chapter chapter;

    private String content;

    public ChapterContent() {
    }

    public ChapterContent(Chapter chapter, String content) {
        this.chapter = chapter;
        this.content = content;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public String getContent() {
        return content;
    }
}
