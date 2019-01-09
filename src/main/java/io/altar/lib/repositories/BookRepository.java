package io.altar.lib.repositories;

import org.springframework.stereotype.Repository;

import io.altar.lib.model.Book;



@Repository
public class BookRepository extends EntityRepository<Book> {

	@Override
	protected Class<Book> getEntityClass() {
		return Book.class;
	}

	@Override
	protected String getAllEntityQueryName() {
		return Book.GET_ALL_BOOKS_QUERY_NAME;
	}

}
