package com.pain.blue.wiki.controller;

import com.pain.blue.rest.response.PageResult;
import com.pain.blue.rest.response.RestResponse;
import com.pain.blue.wiki.domain.dto.CategoryDTO;
import com.pain.blue.wiki.request.category.CategoryIndexRequest;
import com.pain.blue.wiki.request.category.CategorySaveRequest;
import com.pain.blue.wiki.request.category.CategoryUpdateRequest;
import com.pain.blue.wiki.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/categories")
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    public RestResponse index() {
        List<CategoryDTO> result = categoryService.index();
        return RestResponse.success(result);
    }

    @PostMapping("")
    public RestResponse save(@Valid @RequestBody CategorySaveRequest saveRequest) {
        categoryService.save(saveRequest);
        return RestResponse.success();
    }

    @PutMapping("/{id}")
    public RestResponse update(@PathVariable Long id, @Valid @RequestBody CategoryUpdateRequest updateRequest) {
        categoryService.update(id, updateRequest);
        return RestResponse.success();
    }

    @DeleteMapping("/{id}")
    public RestResponse delete(@PathVariable Long id) {
        categoryService.delete(id);
        return RestResponse.success();
    }
}
