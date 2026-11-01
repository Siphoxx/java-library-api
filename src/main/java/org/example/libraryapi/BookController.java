package org.example.libraryapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // 1. GET ALL (Returns a list of DTOs, not Entities)
    @GetMapping
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        // Map every Book entity to a BookDTO
        return books.stream()
                .map(book -> new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.isAvailable()))
                .collect(Collectors.toList());
    }

    // 2. ADD (Accepts a DTO, saves an Entity)
    @PostMapping
    public BookDTO addBook(@RequestBody BookDTO bookDTO) {
        // Convert DTO to Entity
        Book newBook = new Book(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.isAvailable());

        // Save Entity to database
        Book savedBook = bookRepository.save(newBook);

        // Return a DTO based on the saved entity (which now has an ID)
        return new BookDTO(savedBook.getId(), savedBook.getTitle(), savedBook.getAuthor(), savedBook.isAvailable());
    }

    // 3. GET BY ID
    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.isAvailable());
    }

    // 4. UPDATE
    @PutMapping("/{id}")
    public BookDTO updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        // Update the Entity with data from the DTO
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setAvailable(bookDTO.isAvailable());

        Book savedBook = bookRepository.save(existingBook);
        return new BookDTO(savedBook.getId(), savedBook.getTitle(), savedBook.getAuthor(), savedBook.isAvailable());
    }

    // 5. DELETE
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "Book with ID " + id + " has been deleted.";
    }
}