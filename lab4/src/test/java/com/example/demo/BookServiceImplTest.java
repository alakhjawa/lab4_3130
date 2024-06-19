package com.example.demo;
import com.example.demo.Book;
import com.example.demo.BookRepository;
import com.example.demo.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateBook() {
        // Given
        Book newBook = new Book("Clean Code", "Robert C. Martin", "9780132350884");

        // Mocking repository behavior
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        // When
        Book createdBook = bookService.createBook(newBook);

        // Then
        assertNotNull(createdBook);
        assertEquals("Clean Code", createdBook.getTitle());
        assertEquals("Robert C. Martin", createdBook.getAuthor());
        assertEquals("9780132350884", createdBook.getIsbn());
    }

    @Test
    void testGetAllBooks() {
        // Given
        List<Book> books = new ArrayList<>();
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780142437230"));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "9780061120084"));

        // Mocking repository behavior
        when(bookRepository.findAll()).thenReturn(books);

        // When
        List<Book> result = bookService.getAllBooks();

        // Then
        assertEquals(2, result.size());
        assertEquals("The Great Gatsby", result.get(0).getTitle());
        assertEquals("Harper Lee", result.get(1).getAuthor());
    }

    @Test
    void testGetBookById() {
        // Given
        long id = 1L;
        Book book = new Book("1984", "George Orwell", "9780451524935");
        book.setId(id);

        // Mocking repository behavior
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        // When
        Book foundBook = bookService.getBookById(id);

        // Then
        assertNotNull(foundBook);
        assertEquals("1984", foundBook.getTitle());
        assertEquals("George Orwell", foundBook.getAuthor());
        assertEquals("9780451524935", foundBook.getIsbn());
    }



    @Test
    void testUpdateBook_NonExistentId() {
        // Given
        long nonExistentId = 999L;
        Book updatedBook = new Book("Updated Book", "Updated Author", "222");

        // Mocking repository behavior
        when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When
        Book result = bookService.updateBook(nonExistentId, updatedBook);

        // Then
        assertNull(result); // Since the book with nonExistentId doesn't exist, update should return null
    }

    @Test
    void testDeleteBook() {
        // Given
        long id = 1L;

        // Mocking repository behavior
        doNothing().when(bookRepository).deleteById(id);

        // When
        bookService.deleteBook(id);

        // Then
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteBook_CascadeDeleteRelatedEntities() {
        // Given
        long id = 1L;
        Book bookToDelete = new Book("Pride and Prejudice", "Jane Austen", "9780141439518");

        // Mocking repository behavior
        when(bookRepository.findById(id)).thenReturn(Optional.of(bookToDelete));
        doNothing().when(bookRepository).deleteById(id);

        // When
        bookService.deleteBook(id);

    }
}
