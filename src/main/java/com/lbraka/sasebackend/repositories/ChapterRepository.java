package com.lbraka.sasebackend.repositories;

import com.lbraka.sasebackend.model.Chapter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Lauris on 02/06/2017.
 */
public interface ChapterRepository extends CrudRepository<Chapter, Long> {

    Chapter findTop1ByOrderByIdDesc();

    Chapter findTop1ByOrderByIdAsc();

    @Query(value = "SELECT chapter_number, chapter_title FROM Chapter ORDER BY chapter_id ASC", nativeQuery = true)
    List<Object[]> findChaptersTitle();
}
