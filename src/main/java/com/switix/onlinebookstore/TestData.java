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
import java.util.Arrays;
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
    private final OrderStatusRepository orderStatusRepository;

    public TestData(AppUserRepository appUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthorRepository authorRepository, CategoryRepository categoryRepository, BookInventoryRepository bookInventoryRepository, BookRepository bookRepository, CityRepository cityRepository, CountryRepository countryRepository, BillingAddressRepository billingAddressRepository, ShippingAddressRepository shippingAddressRepository, ShoppingSessionRepository shoppingSessionRepository, CartItemRepository cartItemRepository, PayMethodRepository payMethodRepository, ShipmentMethodRepository shipmentMethodRepository, OrderStatusRepository orderStatusRepository) {
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
        this.orderStatusRepository = orderStatusRepository;

        if (appUserRepository.count() == 0) {
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
            orderStatusTestData();
        }


    }

    private void orderStatusTestData() {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setStatus("Nieopłacone");

        OrderStatus orderStatus2 = new OrderStatus();
        orderStatus2.setStatus("Opłacone");

        OrderStatus orderStatus3 = new OrderStatus();
        orderStatus3.setStatus("W realizacji");

        OrderStatus orderStatus4 = new OrderStatus();
        orderStatus4.setStatus("Wysłane");

        orderStatusRepository.save(orderStatus);
        orderStatusRepository.save(orderStatus2);
        orderStatusRepository.save(orderStatus3);
        orderStatusRepository.save(orderStatus4);
    }

    private void shipmentMethodTestData() {
        ShipmentMethod shipmentMethod = new ShipmentMethod();
        shipmentMethod.setName("Kurier Inpost");
        shipmentMethod.setPrice(BigDecimal.ZERO);
        shipmentMethod.setImageUrl("https://inpost.pl/sites/default/files/2021-02/logo_Kurier.png");

        ShipmentMethod shipmentMethod2 = new ShipmentMethod();
        shipmentMethod2.setName("Kurier DPD");
        shipmentMethod2.setPrice(BigDecimal.valueOf(12.30));
        shipmentMethod2.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/DPD_logo%28red%292015.png/800px-DPD_logo%28red%292015.png");

        shipmentMethodRepository.save(shipmentMethod);
        shipmentMethodRepository.save(shipmentMethod2);
    }

    private void payMethodTestData() {
        PayMethod payMethod = new PayMethod();
        payMethod.setName("Przedpłata - Blik");
        payMethod.setImageUrl("https://pep.pl/online/wp-content/uploads/sites/2/2022/04/BLIK-LOGO-RGB.png");

        PayMethod payMethod2 = new PayMethod();
        payMethod2.setName("Przedpłata - PayPal");
        payMethod2.setImageUrl("https://www.citypng.com/public/uploads/preview/transparent-hd-paypal-logo-701751694777788ilpzr3lary.png");

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
        book1.setTitle("Harry Potter i Kamień Filozoficzny");
        book1.setImageUrl("harry_potter_and_the_philosopher_stone.jpg");
        book1.setDescription("Śledź magiczną podróż Harry'ego Pottera, jedenastoletniego chłopca, który w dniu swoich urodzin odkrywa, że jest czarodziejem. Po życiu w zaniedbaniu z jego nienawidzącymi go ciotką i wujkiem, Harry zostaje zaproszony do uczęszczania do Szkoły Magii i Czarodziejstwa w Hogwarcie. Tam znajduje przyjaciół, odkrywa tajemnice swojej przeszłości i staje w obliczu ciemnych sił, próbując powstrzymać powrót złego czarodzieja, Lorda Voldemorta. Ta czarująca opowieść to pierwsza część ukochanej serii o Harrym Potterze, pełna przygód, przyjaźni i odwiecznej walki między dobrem a złem.");
        book1.setPrice(new BigDecimal("78.90"));
        book1.setInventory(bookInventories.get(0));
        book1.setBookAuthors(authors.stream()
                .filter(author -> author.getName().equals("J.K. Rowling"))
                .collect(Collectors.toSet()));
        book1.setCategory(categories.get(0));
        book1.setIsbn("9780747532743");
        book1.setPublicationYear(1997);


        Book book2 = new Book();
        book2.setTitle("1984");
        book2.setImageUrl("1984.jpg");
        book2.setDescription("Kultowa powieść George'a Orwella przedstawiająca dystopijną przyszłość, w której wszechwładna Partia kontroluje każdy aspekt życia obywateli.");
        book2.setPrice(new BigDecimal("39.90"));
        book2.setInventory(bookInventories.get(1));
        book2.setBookAuthors(authors.stream()
                .filter(author -> author.getName().equals("George Orwell"))
                .collect(Collectors.toSet()));
        book2.setCategory(categories.get(4));
        book2.setIsbn("9780748902721");
        book2.setPublicationYear(1949);

        Book book3 = new Book();
        book3.setTitle("It");
        book3.setImageUrl("It.jpg");
        book3.setDescription("„To” to powieść grozy autorstwa Stephena Kinga. Akcja książki toczy się w małym miasteczku Derry w stanie Maine, gdzie dzieci są terroryzowane przez złowrogą istotę przybierającą formę klauna o imieniu Pennywise. Powieść przeplata wątki przeszłości i teraźniejszości, opowiadając o grupie przyjaciół, którzy muszą stawić czoła swojemu największemu koszmarowi zarówno jako dzieci, jak i dorośli. „To” to mistrzowsko skonstruowana opowieść o przyjaźni, odwadze i walce z najgłębszymi lękami.");
        book3.setPrice(new BigDecimal("154.34"));
        book3.setInventory(bookInventories.get(2));
        book3.setBookAuthors(authors.stream()
                .filter(author -> author.getName().contains("Stephen King"))
                .collect(Collectors.toSet()));
        book3.setCategory(categories.get(2));
        book3.setIsbn("9780747512345");
        book3.setPublicationYear(2007);

        Book book4 = new Book();
        book4.setTitle("Martwa strefa");
        book4.setImageUrl("martwa_strefa.jpg");
        book4.setDescription("„Martwa strefa” to jedna z klasycznych powieści Stephena Kinga, w której główny bohater, Johnny Smith, budzi się ze śpiączki po wypadku samochodowym i odkrywa, że posiada zdolność przewidywania przyszłości. Wkrótce odkrywa, że ta zdolność nie jest błogosławieństwem, lecz przekleństwem, gdyż jest zmuszony zmierzyć się z moralnymi dylematami i konsekwencjami swoich przepowiedni.");
        book4.setPrice(new BigDecimal("49.99"));
        book4.setInventory(bookInventories.get(3));
        book4.setBookAuthors(authors.stream()
                .filter(author -> author.getName().contains("Stephen King"))
                .collect(Collectors.toSet()));
        book4.setCategory(categories.get(2));
        book4.setIsbn("9780451167804");
        book4.setPublicationYear(1979);

        Book book5 = new Book();
        book5.setTitle("Shining");
        book5.setImageUrl("shining.jpg");
        book5.setDescription("„Shining” to klasyczna powieść Stephena Kinga, która opowiada historię Jacka Torrance'a, który przyjmuje pracę opiekuna w opuszczonym hotelu Overlook w górach Kolorado. Razem z żoną i synem, Danny'm, Jack staje się świadkiem złowrogiej mocy budynku, która zaczyna wpływać na jego umysł, prowadząc do tragicznych wydarzeń. „Shining” to przerażająca opowieść o izolacji, szaleństwie i duchowych przekleństwach.");
        book5.setPrice(new BigDecimal("59.90"));
        book5.setInventory(bookInventories.get(4));
        book5.setBookAuthors(authors.stream()
                .filter(author -> author.getName().contains("Stephen King"))
                .collect(Collectors.toSet()));
        book5.setCategory(categories.get(2)); // Horror
        book5.setIsbn("9780385121675");
        book5.setPublicationYear(1977);

        Book book6 = new Book();
        book6.setTitle("Zielona mila");
        book6.setImageUrl("zielona_mila.jpg");
        book6.setDescription("„Zielona mila” to poruszająca powieść Stephena Kinga, której akcja toczy się w więzieniu Cold Mountain w Luizjanie w latach 30. XX wieku. Główny bohater, Paul Edgecomb, jest strażnikiem, który staje się świadkiem niezwykłego wydarzenia - pojawienia się więźnia o imieniu John Coffey, który posiada nadprzyrodzone zdolności uzdrawiania. Powieść porusza tematy moralności, sprawiedliwości i nadziei w obliczu ciemności.");
        book6.setPrice(new BigDecimal("69.99"));
        book6.setInventory(bookInventories.get(5));
        book6.setBookAuthors(authors.stream()
                .filter(author -> author.getName().contains("Stephen King"))
                .collect(Collectors.toSet()));
        book6.setCategory(categories.get(2)); // Horror
        book6.setIsbn("9780451933021");
        book6.setPublicationYear(1996);

        Book book7 = new Book();
        book7.setTitle("Talisman");
        book7.setImageUrl("talisman.jpg");
        book7.setDescription("„Talisman” to wspólna powieść Stephena Kinga i Petera Strauba, która opowiada historię chłopca, który wyrusza w niebezpieczną podróż przez mroczny świat równoległy, aby odnaleźć magiczny talizman, który może uratować jego umierającej matce.");
        book7.setPrice(new BigDecimal("79.90"));
        book7.setInventory(bookInventories.get(6));
        book7.setBookAuthors(new HashSet<>(Arrays.asList(authors.get(2), authors.get(3))));
        book7.setCategory(categories.get(1)); // Science Fiction
        book7.setIsbn("9780451212451");
        book7.setPublicationYear(1984);

        Book book8 = new Book();
        book8.setTitle("Duma i uprzedzenie");
        book8.setImageUrl("duma_i_uprzedzenie.jpg");
        book8.setDescription("„Duma i uprzedzenie” to klasyczna powieść Jane Austen, która opowiada historię Elizabeth Bennet i jej dążenia do miłości i szczęścia w XIX-wiecznej Anglii, gdzie społeczne konwenanse i oczekiwania ograniczają jej wybory życiowe.");
        book8.setPrice(new BigDecimal("29.99"));
        book8.setInventory(bookInventories.get(7));
        book8.setBookAuthors(authors.stream()
                .filter(author -> author.getName().equals("Jane Austen"))
                .collect(Collectors.toSet()));
        book8.setCategory(categories.get(3)); // Romans
        book8.setIsbn("9788324072563");
        book8.setPublicationYear(1813);

        Book book9 = new Book();
        book9.setTitle("Harry Potter i Komnata Tajemnic");
        book9.setImageUrl("harry_potter_i_komnata_tajemnic.jpg");
        book9.setDescription("„Harry Potter i Komnata Tajemnic” to druga część serii Harry Potter autorstwa J.K. Rowling. W tej części młody czarodziej Harry Potter powraca do szkoły magii, aby odkryć tajemnice tajemniczej komnaty, która zagraża bezpieczeństwu uczniów Hogwartsu.");
        book9.setPrice(new BigDecimal("39.99"));
        book9.setInventory(bookInventories.get(8));
        book9.setBookAuthors(authors.stream()
                .filter(author -> author.getName().equals("J.K. Rowling"))
                .collect(Collectors.toSet()));
        book9.setCategory(categories.get(0)); // Fantasy
        book9.setIsbn("9788372780126");
        book9.setPublicationYear(1998);

        Book book10 = new Book();
        book10.setTitle("Władca Pierścieni: Drużyna Pierścienia");
        book10.setImageUrl("wladca_pierscieni_druzyna_pierscienia.jpg");
        book10.setDescription("„Władca Pierścieni: Drużyna Pierścienia” to pierwsza część epickiej trylogii fantasy J.R.R. Tolkiena. Książka opowiada historię młodego hobbita, Froda Bagginsa, który wyrusza w niebezpieczną podróż, aby zniszczyć potężny Pierścień, przed którym nawet najbardziej mężni bohaterowie się chylą.");
        book10.setPrice(new BigDecimal("59.90"));
        book10.setInventory(bookInventories.get(9));
        book10.setBookAuthors(authors.stream()
                .filter(author -> author.getName().equals("J.R.R. Tolkien"))
                .collect(Collectors.toSet()));
        book10.setCategory(categories.get(0)); // Fantasy
        book10.setIsbn("9788376486742");
        book10.setPublicationYear(1954);

        Book book11 = new Book();
        book11.setTitle("Gra o tron");
        book11.setImageUrl("gra_o_tron.jpg");
        book11.setDescription("„Gra o tron” to pierwsza książka z serii „Pieśń lodu i ognia” autorstwa George'a R.R. Martina. Akcja toczy się w królestwie Westeros, gdzie szlachetne rody walczą o władzę i przetrwanie w świecie pełnym intryg, intryg i niebezpieczeństw.");
        book11.setPrice(new BigDecimal("79.99"));
        book11.setInventory(bookInventories.get(10));
        book11.setBookAuthors(authors.stream()
                .filter(author -> author.getName().equals("George R.R. Martin"))
                .collect(Collectors.toSet()));
        book11.setCategory(categories.get(1));
        book11.setIsbn("9788375780646");
        book11.setPublicationYear(1996);

        // Książka wielu autorów (3 autorów)
        Book book12 = new Book();
        book12.setTitle("W wysokiej trawie");
        book12.setImageUrl("w_wysokiej_trawie.jpg");
        book12.setDescription("„W wysokiej trawie” to powieść autorstwa Stephena Kinga i Joe Hilla, która przenosi czytelnika w mroczny świat, gdzie dwoje rodzeństwa wpada w pułapkę wysokiej trawy na odległym polu, gdzie czas i przestrzeń nie działają tak, jak powinny. Zagubieni i otoczeni przez trawę, zaczynają słyszeć dziwne głosy i odkrywają, że nie są sami...");
        book12.setPrice(new BigDecimal("59.99"));
        book12.setInventory(bookInventories.get(11));
        book12.setBookAuthors(authors.stream()
                .filter(author -> author.getName().equals("Stephen King") ||
                        author.getName().equals("Joe Hill"))
                .collect(Collectors.toSet()));
        book12.setCategory(categories.get(2));
        book12.setIsbn("9788375780646");
        book12.setPublicationYear(2012);





        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);
        bookRepository.save(book5);
        bookRepository.save(book6);
        bookRepository.save(book7);
        bookRepository.save(book8);
        bookRepository.save(book9);
        bookRepository.save(book10);
        bookRepository.save(book11);
        bookRepository.save(book12);

    }

    private void bookInventoryTestData() {
        BookInventory bookInventory1 = new BookInventory();
        bookInventory1.setQuantity(15);
        BookInventory bookInventory2 = new BookInventory();
        bookInventory2.setQuantity(0);
        BookInventory bookInventory3 = new BookInventory();
        bookInventory3.setQuantity(25);
        BookInventory bookInventory4 = new BookInventory();
        bookInventory4.setQuantity(31);
        BookInventory bookInventory5 = new BookInventory();
        bookInventory5.setQuantity(55);
        BookInventory bookInventory6 = new BookInventory();
        bookInventory6.setQuantity(37);
        BookInventory bookInventory7 = new BookInventory();
        bookInventory7.setQuantity(2);
        BookInventory bookInventory8 = new BookInventory();
        bookInventory8.setQuantity(8);
        BookInventory bookInventory9 = new BookInventory();
        bookInventory9.setQuantity(123);
        BookInventory bookInventory10 = new BookInventory();
        bookInventory10.setQuantity(12);
        BookInventory bookInventory11 = new BookInventory();
        bookInventory11.setQuantity(77);
        BookInventory bookInventory12 = new BookInventory();
        bookInventory12.setQuantity(3);

        bookInventoryRepository.save(bookInventory1);
        bookInventoryRepository.save(bookInventory2);
        bookInventoryRepository.save(bookInventory3);
        bookInventoryRepository.save(bookInventory4);
        bookInventoryRepository.save(bookInventory5);
        bookInventoryRepository.save(bookInventory6);
        bookInventoryRepository.save(bookInventory7);
        bookInventoryRepository.save(bookInventory8);
        bookInventoryRepository.save(bookInventory9);
        bookInventoryRepository.save(bookInventory10);
        bookInventoryRepository.save(bookInventory11);
        bookInventoryRepository.save(bookInventory12);
    }

    private void categoryTestData() {
        Category category1 = new Category();
        category1.setName("Fantasy");

        Category category2 = new Category();
        category2.setName("Science Fiction");

        Category category3 = new Category();
        category3.setName("Horror");

        Category category4 = new Category();
        category4.setName("Romans");

        Category category5 = new Category();
        category5.setName("Powieść dystopijna");

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category4);
        categoryRepository.save(category5);

    }

    private void authorTestData() {
        Author author1 = new Author();
        author1.setName("J.K. Rowling");

        Author author2 = new Author();
        author2.setName("George Orwell");

        Author author3 = new Author();
        author3.setName("Stephen King");

        Author author4 = new Author();
        author4.setName("Peter Straub");

        Author author5 = new Author();
        author5.setName("Jane Austen");

        Author author6 = new Author();
        author6.setName("J.R.R. Tolkien");

        Author author7 = new Author();
        author7.setName("George R.R. Martin");

        Author author8 = new Author();
        author8.setName("Joe Hill");

        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);
        authorRepository.save(author4);
        authorRepository.save(author5);
        authorRepository.save(author6);
        authorRepository.save(author7);
        authorRepository.save(author8);
    }

    private void roleTestData() {
        Role role = new Role();
        role.setName("ROLE_CUSTOMER");

        Role role2 = new Role();
        role2.setName("ROLE_ADMIN");

        roleRepository.save(role);
        roleRepository.save(role2);
    }

    private void appUserTestData() {
        Role roleCustomer = roleRepository.findByName("ROLE_CUSTOMER").get();
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").get();

        AppUser user = new AppUser();
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user"));
        user.setName("Maciej");
        user.setLastname("Świtalski");
        user.setRole(roleCustomer);

        AppUser user2 = new AppUser();
        user2.setEmail("admin@example.com");
        user2.setPassword(passwordEncoder.encode("admin"));
        user2.setName("Maciej");
        user2.setLastname("Świtalski");
        user2.setRole(roleAdmin);


        AppUser user3 = new AppUser();
        user3.setEmail("macie789@wp.pl");
        user3.setPassword(passwordEncoder.encode("user"));
        user3.setName("Maciej");
        user3.setLastname("Świtalski");
        user3.setRole(roleCustomer);

        appUserRepository.save(user);
        appUserRepository.save(user2);
        appUserRepository.save(user3);
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
