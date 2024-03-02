package com.switix.onlinebookstore;

import com.switix.onlinebookstore.model.*;
import com.switix.onlinebookstore.repository.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class TestData {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookInventoryRepository bookInventoryRepository;
    private final BookRepository bookRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final BillingAddressRepository billingAddressRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final ShoppingSessionRepository shoppingSessionRepository;
    private final CartItemRepository cartItemRepository;
    private final PayMethodRepository payMethodRepository;
    private final ShipmentMethodRepository shipmentMethodRepository;

    public TestData(AppUserRepository appUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthorRepository authorRepository, CategoryRepository categoryRepository, BookInventoryRepository bookInventoryRepository, BookRepository bookRepository, CityRepository cityRepository, CountryRepository countryRepository, BillingAddressRepository billingAddressRepository, ShippingAddressRepository shippingAddressRepository, ShoppingSessionRepository shoppingSessionRepository, CartItemRepository cartItemRepository, PayMethodRepository payMethodRepository, ShipmentMethodRepository shipmentMethodRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.bookInventoryRepository = bookInventoryRepository;
        this.bookRepository = bookRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.billingAddressRepository = billingAddressRepository;
        this.shippingAddressRepository = shippingAddressRepository;
        this.shoppingSessionRepository = shoppingSessionRepository;
        this.cartItemRepository = cartItemRepository;
        this.payMethodRepository = payMethodRepository;
        this.shipmentMethodRepository = shipmentMethodRepository;

        authorTestData();
        categoryTestData();
        bookInventoryTestData();
        bookTestData();
        roleTestData();
        populateCityTable();
        countryTestData();
        appUserTestData();
        billingAddressTestData();
        shoppingSessionTestData();
        cartItemTestData();
        shippingAddressTestData();
        payMethodTestData();
        shipmentMethodTestData();

    }

    private void shipmentMethodTestData() {
        ShipmentMethod shipmentMethod = new ShipmentMethod();
        shipmentMethod.setName("Kurier Inpost");
        shipmentMethod.setPrice(BigDecimal.ZERO);
        shipmentMethod.setImageUrl("https://inpost.pl/sites/default/files/2021-02/logo_Kurier.png");

        ShipmentMethod shipmentMethod2 = new ShipmentMethod();
        shipmentMethod2.setName("Kurier DPD");
        shipmentMethod2.setPrice(BigDecimal.valueOf(12.30));
        shipmentMethod2.setImageUrl("https://inpost.pl/sites/default/files/2021-02/logo_Kurier.png");

        shipmentMethodRepository.save(shipmentMethod);
        shipmentMethodRepository.save(shipmentMethod2);
    }

    private void payMethodTestData() {
        PayMethod payMethod = new PayMethod();
        payMethod.setName("Przedpłata - Blik");
        payMethod.setImageUrl("https://pep.pl/online/wp-content/uploads/sites/2/2022/04/BLIK-LOGO-RGB.png");

        PayMethod payMethod2 = new PayMethod();
        payMethod2.setName("Przedpłata - PayPal");
        payMethod2.setImageUrl("https://pep.pl/online/wp-content/uploads/sites/2/2022/04/BLIK-LOGO-RGB.png");

        payMethodRepository.save(payMethod);
        payMethodRepository.save(payMethod2);
    }

    private void cartItemTestData() {
        List<Book> books = bookRepository.findAll();
        ShoppingSession shoppingSessionWithItems = shoppingSessionRepository.findById(2L).get();

        CartItem cartItem = new CartItem();
        cartItem.setBook(books.get(0));
        cartItem.setQuantity(1);
        cartItem.setShoppingSession(shoppingSessionWithItems);

        CartItem cartItem1 = new CartItem();
        cartItem1.setBook(books.get(2));
        cartItem1.setQuantity(2);
        cartItem1.setShoppingSession(shoppingSessionWithItems);

        BigDecimal total = cartItem.getBook().getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                .add(cartItem1.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem1.getQuantity())));
        shoppingSessionWithItems.setTotal(total);

        shoppingSessionRepository.save(shoppingSessionWithItems);
        cartItemRepository.save(cartItem);
        cartItemRepository.save(cartItem1);
    }

    private void shoppingSessionTestData() {
        AppUser userWithCartItems = appUserRepository.findByEmail("user@example.com").get();
        AppUser userWithoutCartItems = appUserRepository.findByEmail("macie789@wp.pl").get();

        ShoppingSession shoppingSession = new ShoppingSession();
        shoppingSession.setTotal(BigDecimal.ZERO);
        shoppingSession.setAppUser(userWithoutCartItems);

        ShoppingSession shoppingSession1 = new ShoppingSession();
        shoppingSession1.setTotal(BigDecimal.ZERO);
        shoppingSession1.setAppUser(userWithCartItems);

        shoppingSessionRepository.save(shoppingSession);
        shoppingSessionRepository.save(shoppingSession1);

    }

    private void billingAddressTestData() {
        Country country = countryRepository.findByCountryName("Polska").get();
        City city = cityRepository.findByCityName("Nakło nad Notecią").get();
        AppUser appUser = appUserRepository.findByEmail("user@example.com").get();


        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setCity(city);
        billingAddress.setCountry(country);
        billingAddress.setPhoneNumber("123456789");
        billingAddress.setStreet("Nowa");
        billingAddress.setBuildingNumber("35");
        billingAddress.setZipCode("89-100");
        billingAddress.setApartmentNumber("3");
        billingAddress.setAppUser(appUser);

        billingAddressRepository.save(billingAddress);
    }
    private void shippingAddressTestData() {
        Country country = countryRepository.findByCountryName("Polska").get();
        City city = cityRepository.findByCityName("Nakło nad Notecią").get();
        AppUser appUser = appUserRepository.findByEmail("user@example.com").get();

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setCity(city);
        shippingAddress.setCountry(country);
        shippingAddress.setName("dom");
        shippingAddress.setStreet("Nowa");
        shippingAddress.setBuildingNumber("35");
        shippingAddress.setZipCode("89-100");
        shippingAddress.setApartmentNumber("3");
        shippingAddress.setAppUser(appUser);

        shippingAddressRepository.save(shippingAddress);
    }

    private void countryTestData() {
        Country country = new Country();
        country.setCountryName("Polska");
        countryRepository.save(country);
    }

    private void bookTestData() {

        List<BookInventory> bookInventories = bookInventoryRepository.findAll();
        List<Author> authors = authorRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        Book book1 = new Book();
        book1.setTitle("Harry Potter and the Philosopher's Stone");
        book1.setImageUrl("harry_potter_and_the_philosopher_stone.jpg");
        book1.setDescription("A young wizard's journey at Hogwarts.");
        book1.setPrice(new BigDecimal("78.90"));
        book1.setInventory(bookInventories.get(0));
        book1.setBookAuthors(authors.stream()
                .filter(author -> author.getName().equals("J.K. Rowling"))
                .collect(Collectors.toSet()));
        book1.setCategory(categories.get(1));
        book1.setIsbn("9780747532743");
        book1.setPublicationYear(1997);


        Book book2 = new Book();
        book2.setTitle("1984");
        book2.setImageUrl("harry_potter_and_the_philosopher_stone.jpg");
        book2.setDescription("A dystopian novel about a totalitarian regime.");
        book2.setPrice(new BigDecimal("109.99"));
        book2.setInventory(bookInventories.get(1));
        book2.setBookAuthors(new HashSet<>(authors));
        book2.setCategory(categories.get(1));
        book2.setIsbn("9780748902721");
        book2.setPublicationYear(2001);

        Book book3 = new Book();
        book3.setTitle("It");
        book3.setImageUrl("It.jpg");
        book3.setDescription("A horror novel by Stephen King.");
        book3.setPrice(new BigDecimal("154.34"));
        book3.setInventory(bookInventories.get(2));
        book3.setBookAuthors(authors.stream()
                .filter(author -> author.getName().contains("Stephen King"))
                .collect(Collectors.toSet()));
        book3.setCategory(categories.get(2));
        book3.setIsbn("9780747512345");
        book3.setPublicationYear(2007);

        Book book4 = new Book();
        book4.setTitle("It");
        book4.setImageUrl("It.jpg");
        book4.setDescription("A horror novel by Stephen King.");
        book4.setPrice(new BigDecimal("154.34"));
        book4.setInventory(bookInventories.get(2));
        book4.setBookAuthors(authors.stream()
                .filter(author -> author.getName().contains("Stephen King"))
                .collect(Collectors.toSet()));
        book4.setCategory(categories.get(2));
        book4.setIsbn("9780747538907");
        book4.setPublicationYear(2009);

        Book book5 = new Book();
        book5.setTitle("It");
        book5.setImageUrl("It.jpg");
        book5.setDescription("A horror novel by Stephen King.");
        book5.setPrice(new BigDecimal("154.34"));
        book5.setInventory(bookInventories.get(2));
        book5.setBookAuthors(authors.stream()
                .filter(author -> author.getName().contains("Stephen King"))
                .collect(Collectors.toSet()));
        book5.setCategory(categories.get(2));
        book5.setIsbn("9780747535748");
        book5.setPublicationYear(2012);

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
        user.setName("Maciej");
        user.setLastname("Świtalski");
        user.setRole(role);


        AppUser user2 = new AppUser();
        user2.setEmail("macie789@wp.pl");
        user2.setPassword(passwordEncoder.encode("user"));
        user2.setName("Maciej");
        user2.setLastname("Świtalski");
        user2.setRole(role);

        appUserRepository.save(user);
        appUserRepository.save(user2);
    }

    private void populateCityTable() {
        String csvFile = "src\\main\\resources\\cities\\poland.csv";
        List<City> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 2) {
                    String cityName = columns[0].trim();
                    City city = new City();
                    city.setCityName(cityName);
                    cities.add(city);

                } else {
                    System.err.println("Incomplete line or missing columns: " + line);
                }
            }
            cityRepository.saveAll(cities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
