package com.pain.blue.wiki.controller;

import com.pain.blue.rest.response.PageResult;
import com.pain.blue.rest.response.RestResponse;
import com.pain.blue.wiki.domain.dto.BookDTO;
import com.pain.blue.wiki.query.BookIndexQuery;
import com.pain.blue.wiki.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/books")
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("")
    public RestResponse index(BookIndexQuery query) {
        PageResult<BookDTO> result = bookService.index(query);
        return RestResponse.success(result);
    }
}
