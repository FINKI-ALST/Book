package mk.ukim.finki.wp.exam.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import mk.ukim.finki.wp.exam.example.model.Genre;
import mk.ukim.finki.wp.exam.example.model.Book;
import mk.ukim.finki.wp.exam.example.model.Role;
import mk.ukim.finki.wp.exam.example.selenium.AbstractPage;
import mk.ukim.finki.wp.exam.example.selenium.AddOrEditBook;
import mk.ukim.finki.wp.exam.example.selenium.ItemsPage;
import mk.ukim.finki.wp.exam.example.selenium.LoginPage;
import mk.ukim.finki.wp.exam.example.service.GenreService;
import mk.ukim.finki.wp.exam.example.service.BookService;
import mk.ukim.finki.wp.exam.example.service.UserService;
import mk.ukim.finki.wp.exam.util.CodeExtractor;
import mk.ukim.finki.wp.exam.util.ExamAssert;
import mk.ukim.finki.wp.exam.util.SubmissionHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.List;
import java.util.NoSuchElementException;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SeleniumScenarioTest {

    @Autowired
    UserService userService;

    @Autowired
    GenreService genreService;

    @Autowired
    BookService bookService;

    @Test
    public void testServiceInit(){
        ExamAssert.assertEquals("books not", 10, this.bookService.listAllBooks().size());
    }
    @Test
    public void test1_list() {
        SubmissionHelper.startTest("test-list-20");
        List<Book> books = this.bookService.listAllBooks();
        int itemNum = books.size();

        ItemsPage booksPage = ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");
        booksPage.assertItems(itemNum);

        SubmissionHelper.endTest();
    }

    @Test
    public void test2_filter() {
        SubmissionHelper.startTest("test-filter-20");
        ExamAssert.assertEquals("without filter", 10, this.bookService.listBooksByNameAndGenre(null, null).size());
        ExamAssert.assertEquals("by name only", 2, this.bookService.listBooksByNameAndGenre("uct 1", null).size());
        ExamAssert.assertEquals("by category only", 10, this.bookService.listBooksByNameAndGenre(null, 1L).size());
        ExamAssert.assertEquals("by name and category", 2, this.bookService.listBooksByNameAndGenre("uct 1", 1L).size());
        SubmissionHelper.endTest();
    }

    @Test
    public void test3_create() {
        SubmissionHelper.startTest("test-create-20");
        List<Genre> genres = this.genreService.listAll();
        List<Book> books = this.bookService.listAllBooks();

        int itemNum = books.size();

        String[] bookGenres = new String[]{
                genres.get(0).getId().toString(),
                genres.get(genres.size() - 1).getId().toString()
        };

        ItemsPage booksPage = null;
        try {
            LoginPage loginPage = LoginPage.openLogin(this.driver);
            booksPage = LoginPage.doLogin(this.driver, loginPage, admin, admin);
        } catch (Exception e) {
        }
        booksPage = AddOrEditBook.add(this.driver, PRODUCTS_ADD_URL, "test", "100", "5", bookGenres);
        AbstractPage.assertRelativeUrl(this.driver, PRODUCTS_URL);
        booksPage.assertItems(itemNum + 1);

        SubmissionHelper.endTest();
    }

    @Test
    public void test4_edit() throws Exception {
        SubmissionHelper.startTest("test-edit-20");
        List<Genre> genres = this.genreService.listAll();
        List<Book> books = this.bookService.listAllBooks();

        int itemNum = books.size();

        String[] bookGenres = new String[]{
                genres.get(0).getId().toString(),
                genres.get(genres.size() - 1).getId().toString()
        };

        ItemsPage booksPage = null;
        try {
            LoginPage loginPage = LoginPage.openLogin(this.driver);
            booksPage = LoginPage.doLogin(this.driver, loginPage, admin, admin);
            booksPage = AddOrEditBook.update(this.driver, booksPage.getEditButtons().get(itemNum - 1), "test1", "200", "4", bookGenres);
            AbstractPage.assertRelativeUrl(this.driver, PRODUCTS_URL);
            ExamAssert.assertEquals("The updated product name is not as expected.", "test1", booksPage.getProductRows().get(itemNum - 1).findElements(By.tagName("td")).get(0).getText().trim());
            booksPage.assertItems(itemNum);
        } catch (Exception e) {
            MockHttpServletRequestBuilder bookEditRequest = MockMvcRequestBuilders
                    .post("/books/" + books.get(itemNum - 1).getId())
                    .param("name", "test1")
                    .param("price", "200")
                    .param("quantity", "4");
            for (Genre g : genres) {
                bookEditRequest = bookEditRequest.param("genres", g.getId().toString());
            }

            this.mockMvc.perform(bookEditRequest)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                    .andExpect(MockMvcResultMatchers.redirectedUrl(PRODUCTS_URL));
            books = this.bookService.listAllBooks();
            ExamAssert.assertEquals("Number of items", itemNum, books.size());
            ExamAssert.assertEquals("The updated book name is not as expected.", "test1", books.get(itemNum - 1).getName());
        }

        SubmissionHelper.endTest();
    }

    @Test
    public void test5_delete() throws Exception {
        SubmissionHelper.startTest("test-delete-10");
        List<Book> books = this.bookService.listAllBooks();
        int itemNum = books.size();

        ItemsPage booksPage = null;
        try {
            LoginPage loginPage = LoginPage.openLogin(this.driver);
            booksPage = LoginPage.doLogin(this.driver, loginPage, admin, admin);
            booksPage.getDeleteButtons().get(itemNum - 1).click();
            AbstractPage.assertRelativeUrl(this.driver, PRODUCTS_URL);
            booksPage.assertItems(itemNum - 1);
        } catch (Exception e) {
            MockHttpServletRequestBuilder productDeleteRequest = MockMvcRequestBuilders
                    .post("/books/" + books.get(itemNum - 1).getId() + "/delete");

            this.mockMvc.perform(productDeleteRequest)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                    .andExpect(MockMvcResultMatchers.redirectedUrl(PRODUCTS_URL));
            books = this.bookService.listAllBooks();
            ExamAssert.assertEquals("Number of items", itemNum - 1, books.size());
        }
        SubmissionHelper.endTest();
    }

    @Test
    public void test6_security_urls() {
        SubmissionHelper.startTest("test-security-urls-10");
        List<Book> books = this.bookService.listAllBooks();
        int itemNum = books.size();
        String editUrl = "/books/" + books.get(0).getId() + "/edit";

        ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");

        AbstractPage.get(this.driver, PRODUCTS_URL);
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);
        AbstractPage.get(this.driver, PRODUCTS_ADD_URL);
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);
        AbstractPage.get(this.driver, editUrl);
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);
        AbstractPage.get(this.driver, "/random");
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);

        LoginPage loginPage = LoginPage.openLogin(this.driver);
        LoginPage.doLogin(this.driver, loginPage, admin, admin);
        AbstractPage.assertRelativeUrl(this.driver, PRODUCTS_URL);


        AbstractPage.get(this.driver, PRODUCTS_URL);
        AbstractPage.assertRelativeUrl(this.driver, PRODUCTS_URL);

        AbstractPage.get(this.driver, PRODUCTS_ADD_URL);
        AbstractPage.assertRelativeUrl(this.driver, PRODUCTS_ADD_URL);

        AbstractPage.get(this.driver, editUrl);
        AbstractPage.assertRelativeUrl(this.driver, editUrl);

        LoginPage.logout(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");

        SubmissionHelper.endTest();
    }

    @Test
    public void test7_security_buttons() {
        SubmissionHelper.startTest("test-security-buttons-10");
        List<Book> books = this.bookService.listAllBooks();
        int itemNum = books.size();

        ItemsPage booksPage = ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");
        booksPage.assertButtons(0, 0, 0);

        LoginPage loginPage = LoginPage.openLogin(this.driver);
        booksPage = LoginPage.doLogin(this.driver, loginPage, admin, admin);
        booksPage.assertButtons(itemNum, itemNum, 1);
        SubmissionHelper.endTest();
    }


    private HtmlUnitDriver driver;
    private MockMvc mockMvc;

    private static String admin = "testAdmin";

    private static boolean dataInitialized = false;


    @BeforeEach
    private void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.driver = new HtmlUnitDriver(true);
        initData();
    }

    @AfterEach
    public void destroy() {
        if (this.driver != null) {
            this.driver.close();
        }
    }


    private void initData() {
        if (!dataInitialized) {
            userService.create(admin, admin, Role.ROLE_ADMIN);
            dataInitialized = true;
        }
    }


    @AfterAll
    public static void finializeAndSubmit() throws JsonProcessingException {
        CodeExtractor.submitSourcesAndLogs();
    }


    public static final String PRODUCTS_URL = "/books";
    public static final String PRODUCTS_ADD_URL = "/books/add";
    public static final String LOGIN_URL = "/login";
}
