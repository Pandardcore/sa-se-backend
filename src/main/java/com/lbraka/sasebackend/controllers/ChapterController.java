package com.lbraka.sasebackend.controllers;

/**
 * Created by Lauris on 19/05/2017.
 */

import com.lbraka.sasebackend.controllers.dto.ChapterContent;
import com.lbraka.sasebackend.model.Chapter;
import com.lbraka.sasebackend.model.Page;
import com.lbraka.sasebackend.repositories.ChapterRepository;
import com.lbraka.sasebackend.repositories.PageRepository;
import com.lbraka.sasebackend.services.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

@RestController
@RequestMapping("/chapters")
@CrossOrigin
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private ChapterRepository chapterRepo;

    @Autowired
    private PageRepository pageRepository;

    public ChapterController() { }

    @RequestMapping(method = RequestMethod.POST)
    public Chapter newChapter(@RequestBody ChapterContent newChapter) {
        newChapter.getChapter().setPages(chapterService.buildChapterPages(newChapter.getContent()));
        Iterable<Page> save1 = pageRepository.save(newChapter.getChapter().getPages());
        Chapter save = chapterRepo.save(newChapter.getChapter());
        return save;
    }

    @RequestMapping(method = RequestMethod.GET, value="{id}")
    public Chapter chapterById(@PathVariable Long id) {
        Chapter chapter = this.chapterRepo.findOne(id);
        if(chapter != null) {
            chapter.getPages();
        }
        return chapter;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/last")
    public Chapter lastChapter() {
        Iterable<Chapter> allChapters = this.chapterRepo.findAll();
        if(allChapters != null) {
            Iterator<Chapter> iterator = allChapters.iterator();
            if(iterator.hasNext()) {
                Chapter chapter = iterator.next();
                Iterable<Page> allPages = pageRepository.findAll();
                Iterator<Page> iterator1 = allPages.iterator();
                while(iterator1.hasNext()) {
                    chapter.getPages().add(iterator1.next());
                }
                return chapter;
            }
        }
        return null;
    }
}
