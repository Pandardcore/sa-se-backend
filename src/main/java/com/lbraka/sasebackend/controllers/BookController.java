package com.lbraka.sasebackend.controllers;

import com.lbraka.sasebackend.model.Book;
import com.lbraka.sasebackend.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Lauris on 14/05/2017.
 */
@RestController
@RequestMapping("/books")
@CrossOrigin
public class BookController {

    @Autowired
    private BookRepository bookRepo;

    public BookController() {
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Book> books() {
        return this.bookRepo.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value="{id}")
    public Book booksById(@PathVariable Long id) {
        Book book = this.bookRepo.findOne(id);
        if(book != null) {
            book.getChapters();
        }
        return book;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Book newBook(@RequestBody Book newBook) {
        return bookRepo.save(newBook);
    }
}
