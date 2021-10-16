package com.pain.blue.wiki.controller;

import com.pain.blue.rest.response.PageResult;
import com.pain.blue.rest.response.RestResponse;
import com.pain.blue.wiki.domain.dto.BookDTO;
import com.pain.blue.wiki.request.book.BookIndexRequest;
import com.pain.blue.wiki.request.book.BookSaveRequest;
import com.pain.blue.wiki.request.book.BookUpdateRequest;
import com.pain.blue.wiki.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/books")
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("")
    public RestResponse index(@Valid BookIndexRequest query) {
        PageResult<BookDTO> result = bookService.index(query);
        return RestResponse.success(result);
    }

    @PostMapping("")
    public RestResponse save(@Valid @RequestBody BookSaveRequest saveRequest) {
        bookService.save(saveRequest);
        return RestResponse.success();
    }

    @PutMapping("/{id}")
    public RestResponse update(@PathVariable Long id, @Valid @RequestBody BookUpdateRequest updateRequest) {
        bookService.update(id, updateRequest);
        return RestResponse.success();
    }

    @DeleteMapping("/{id}")
    public RestResponse delete(@PathVariable Long id) {
        bookService.delete(id);
        return RestResponse.success();
    }
}
