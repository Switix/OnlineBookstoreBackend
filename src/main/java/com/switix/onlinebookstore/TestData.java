package com.switix.onlinebookstore;

import com.switix.onlinebookstore.model.*;
import com.switix.onlinebookstore.repository.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class TestData {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private  final PasswordEncoder passwordEncoder;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookInventoryRepository  bookInventoryRepository;
    private final BookRepository bookRepository;

    public TestData(AppUserRepository appUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthorRepository authorRepository, CategoryRepository categoryRepository, BookInventoryRepository bookInventoryRepository, BookRepository bookRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.bookInventoryRepository = bookInventoryRepository;
        this.bookRepository = bookRepository;

        roleTestData();
        appUserTestData();
        authorTestData();
        categoryTestData();
        bookInventoryTestData();
        bookTestData();
    }

    private void bookTestData() {

        List<BookInventory> bookInventories = bookInventoryRepository.findAll();
        List<Author> authors = authorRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        Book book1 = new Book();
        book1.setTitle("Harry Potter and the Philosopher's Stone");
        book1.setDescription("A young wizard's journey at Hogwarts.");
        book1.setPrice(new BigDecimal("78.90"));
        book1.setInventory(bookInventories.get(0));
        book1.setBookAuthors(authors.stream()
                .filter(author -> author.getName().equals("J.K. Rowling"))
                .collect(Collectors.toSet()));
        book1.setCategory(categories.get(1));


        Book book2 = new Book();
        book2.setTitle("1984");
        book2.setDescription("A dystopian novel about a totalitarian regime.");
        book2.setPrice(new BigDecimal("109.99"));
        book2.setInventory(bookInventories.get(1));
        book2.setBookAuthors(new HashSet<>(authors));
        book2.setCategory(categories.get(1));

        Book book3 = new Book();
        book3.setTitle("It");
        book3.setDescription( "A horror novel by Stephen King.");
        book3.setPrice(new BigDecimal("154.34"));
        book3.setInventory(bookInventories.get(2));
        book3.setBookAuthors(authors.stream()
                .filter(author -> author.getName().contains("Stephen King"))
                .collect(Collectors.toSet()));
        book3.setCategory(categories.get(2));

        Book book4 = new Book();
        book4.setTitle("It");
        book4.setDescription( "A horror novel by Stephen King.");
        book4.setPrice(new BigDecimal("154.34"));
        book4.setInventory(bookInventories.get(2));
        book4.setBookAuthors(authors.stream()
                .filter(author -> author.getName().contains("Stephen King"))
                .collect(Collectors.toSet()));
        book4.setCategory(categories.get(2));

        Book book5 = new Book();
        book5.setTitle("It");
        book5.setDescription( "A horror novel by Stephen King.");
        book5.setPrice(new BigDecimal("154.34"));
        book5.setInventory(bookInventories.get(2));
        book5.setBookAuthors(authors.stream()
                .filter(author -> author.getName().contains("Stephen King"))
                .collect(Collectors.toSet()));
        book5.setCategory(categories.get(2));

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);
        bookRepository.save(book5);
    }

    private void bookInventoryTestData() {
        BookInventory bookInventory1 = new BookInventory();
        bookInventory1.setQuantity(15);
        BookInventory bookInventory2 = new BookInventory();
        bookInventory2.setQuantity(0);
        BookInventory bookInventory3 = new BookInventory();
        bookInventory3.setQuantity(25);
        bookInventoryRepository.save(bookInventory1);
        bookInventoryRepository.save(bookInventory2);
        bookInventoryRepository.save(bookInventory3);
    }

    private void categoryTestData() {
        Category category1 = new Category();
        category1.setName("Fantasy");
        Category category2 = new Category();
        category2.setName("Science Fiction");
        Category category3 = new Category();
        category3.setName("Horror");

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
    }

    private void authorTestData() {
        Author author1 = new Author();
        author1.setName("J.K. Rowling");

        Author author2 = new Author();
        author2.setName("George Orwell");

        Author author3 = new Author();
        author3.setName("Stephen King");

        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);
    }

    private void roleTestData() {
        Role role = new Role();
        role.setName("ROLE_CUSTOMER");
        roleRepository.save(role);
    }

    private void appUserTestData() {
        Role role = roleRepository.findByName("ROLE_CUSTOMER").get();

        AppUser user = new AppUser();
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user"));
        user.setUsername("user");
        user.setRole(role);

        appUserRepository.save(user);
    }
}
