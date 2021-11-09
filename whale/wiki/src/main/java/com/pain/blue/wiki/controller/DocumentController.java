package com.pain.blue.wiki.controller;

import com.pain.blue.rest.response.RestResponse;
import com.pain.blue.wiki.domain.dto.CategoryDTO;
import com.pain.blue.wiki.domain.dto.DocumentDTO;
import com.pain.blue.wiki.request.category.CategorySaveRequest;
import com.pain.blue.wiki.request.category.CategoryUpdateRequest;
import com.pain.blue.wiki.request.document.DocumentSaveRequest;
import com.pain.blue.wiki.request.document.DocumentUpdateRequest;
import com.pain.blue.wiki.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/documents")
@RestController
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("")
    public RestResponse index(@RequestParam("bookId") long bookId) {
        List<DocumentDTO> result = documentService.index(bookId);
        return RestResponse.success(result);
    }

    @PostMapping("")
    public RestResponse save(@Valid @RequestBody DocumentSaveRequest saveRequest) {
        documentService.save(saveRequest);
        return RestResponse.success();
    }

    @PutMapping("/{id}")
    public RestResponse update(@PathVariable Long id, @Valid @RequestBody DocumentUpdateRequest updateRequest) {
        documentService.update(id, updateRequest);
        return RestResponse.success();
    }

    @DeleteMapping("/{id}")
    public RestResponse delete(@PathVariable Long id) {
        documentService.delete(id);
        return RestResponse.success();
    }
}
