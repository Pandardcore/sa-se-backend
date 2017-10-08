package com.lbraka.sasebackend;

import com.lbraka.sasebackend.model.Book;
import com.lbraka.sasebackend.model.Chapter;
import com.lbraka.sasebackend.repositories.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SaSeBackendApplicationTests {

//	@Autowired
//	private BookRepository repo;

//	@Test
//	public void contextLoads() {
//		Book book = new Book(null, "Test", "Test", null);
//		repo.save(book);
//		Iterable<Book> all = repo.findAll();
//		assertThat(all).isNotNull().isNotEmpty();
//	}

	@Test
	public void testPages() {
		System.out.println("Comment vas-tu ? Ca va bien !"
				.replaceAll("(\\s\\?)", "?")
                .replaceAll("(\\s!)", "!"));
	}

}
