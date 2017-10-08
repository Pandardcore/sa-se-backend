package com.lbraka.sasebackend.controllers;

/**
 * Created by Lauris on 19/05/2017.
 */

import com.lbraka.sasebackend.controllers.dto.ChapterContent;
import com.lbraka.sasebackend.controllers.dto.ChapterTitle;
import com.lbraka.sasebackend.model.Chapter;
import com.lbraka.sasebackend.model.Page;
import com.lbraka.sasebackend.repositories.ChapterRepository;
import com.lbraka.sasebackend.repositories.PageRepository;
import com.lbraka.sasebackend.services.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
        Chapter savedChapter = chapterService.createNewChapter(newChapter.getChapter(), newChapter.getContent(), lastChapter());
        pageRepository.save(savedChapter.getPages());
        return savedChapter;
    }

    @RequestMapping(method = RequestMethod.GET, value="{id}")
    public Chapter chapterById(@PathVariable Long id) {
        return this.chapterRepo.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/last")
    public Chapter lastChapter() {
        Chapter top1ByOrderByIdDesc = chapterRepo.findTop1ByOrderByIdDesc();
        return chapterRepo.findTop1ByOrderByIdDesc();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/first")
    public Chapter firstChapter() {
        return chapterRepo.findTop1ByOrderByIdAsc();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/titles")
    public List<ChapterTitle> chapterTitles() {
        return chapterRepo.findChaptersTitle().stream()
                .map(obj -> new ChapterTitle(obj[0].toString(), obj[1].toString())).collect(Collectors.toList());
    }
}
