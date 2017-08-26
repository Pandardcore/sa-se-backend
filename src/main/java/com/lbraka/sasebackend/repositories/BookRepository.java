package com.lbraka.sasebackend.repositories;

import com.lbraka.sasebackend.model.Book;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Lauris on 14/05/2017.
 */
public interface BookRepository extends CrudRepository<Book, Long> {
}
