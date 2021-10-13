package com.pain.blue.wiki.controller;

import com.pain.blue.rest.response.PageResult;
import com.pain.blue.rest.response.RestResponse;
import com.pain.blue.wiki.domain.dto.BookDTO;
import com.pain.blue.wiki.request.BookIndexRequest;
import com.pain.blue.wiki.request.BookUpdateRequest;
import com.pain.blue.wiki.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/books")
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("")
    public RestResponse index(BookIndexRequest query) {
        PageResult<BookDTO> result = bookService.index(query);
        return RestResponse.success(result);
    }

    @PutMapping("/{id}")
    public RestResponse update(@PathVariable Long id, @RequestBody BookUpdateRequest updateRequest) {
        bookService.update(id, updateRequest);
        return RestResponse.success();
    }
}
