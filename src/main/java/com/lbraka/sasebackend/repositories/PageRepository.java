package com.lbraka.sasebackend.repositories;

import com.lbraka.sasebackend.model.Page;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Lauris on 22/08/2017.
 */
public interface PageRepository extends CrudRepository<Page, Long> {

    List<Page> findByChapterId(Long chapterId);
}
