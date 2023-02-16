package com.matsa.controllers;

import com.matsa.AbstractControllerTest;
import com.matsa.dto.BookDto;
import com.matsa.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class BookControllerTest extends AbstractControllerTest {
    @Test
    void createBook() throws Exception {
        BookDto testBook = mockBook();
        String json = objectMapper.writeValueAsString(testBook);
        MvcResult mvcResult = mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        Book bookResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Book.class);
        assertThat(bookResult.getId(), notNullValue());
        assertThat(bookResult.getName(), equalTo(testBook.getName()));
        assertThat(bookResult.getAuthor(), equalTo(testBook.getAuthor()));
        assertThat(bookResult.getDescription(), equalTo(testBook.getDescription()));
        assertThat(bookResult.getPublisher(), equalTo(testBook.getPublisher()));
        assertThat(bookResult.getIsbn(), equalTo(testBook.getIsbn()));
        assertThat(bookResult.getYear(), equalTo(testBook.getYear()));
    }

    @Test
    void findBook() throws Exception {
        BookDto testBook = mockBook();
        String json = objectMapper.writeValueAsString(testBook);
        mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(get("/book").param("page", "0").param("size", "1").param("query", "asda"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", empty()))
                .andReturn();
        mockMvc.perform(get("/book").param("page", "0").param("size", "1").param("query", "author"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andReturn();

    }

    @Test
    void createBookValidation() throws Exception {
        BookDto testBook = mockBook();
        testBook.setName(null);
        testBook.setAuthor("");
        mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBook)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", containsInAnyOrder("name is mandatory", "author is mandatory")));
        testBook = mockBook();
        testBook.setIsbn("wrong format");
        mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBook)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", containsInAnyOrder("isbn must match \"^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$\"")));
    }

    @Test
    void updateBookValidation() throws Exception {
        BookDto testBook = mockBook();
        testBook.setName(null);
        testBook.setAuthor("");
        mockMvc.perform(put("/book/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBook)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", containsInAnyOrder("name is mandatory", "author is mandatory")));
        testBook = mockBook();
        testBook.setIsbn("wrong format");
        mockMvc.perform(put("/book/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBook)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", containsInAnyOrder("isbn must match \"^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$\"")));
    }

    @Test
    void getNotExistingBook() throws Exception {
        mockMvc.perform(get("/book/123"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("status", equalTo(HttpStatus.NOT_FOUND.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", contains("Book not found")));
    }

    private BookDto mockBook() {
        BookDto testBook = new BookDto();
        testBook.setName("Test name");
        testBook.setDescription("Test description");
        testBook.setAuthor("Test author");
        testBook.setPublisher("Test publisher");
        testBook.setIsbn("978-617-7866-64-9");
        testBook.setYear(2000);
        return testBook;
    }
}