package com.example.demo;
import org.springframework.stereotype.Service;
import java.util.List;

public interface BookService {

    Book createBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(Long id);
    Book updateBook(Long id, Book book);
    void deleteBook(Long id);
}