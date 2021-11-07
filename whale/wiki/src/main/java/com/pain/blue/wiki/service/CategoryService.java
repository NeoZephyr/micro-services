package com.pain.blue.wiki.service;

import com.pain.blue.id.IdGenerator;
import com.pain.blue.mapping.CopyUtils;
import com.pain.blue.wiki.domain.dto.CategoryDTO;
import com.pain.blue.wiki.domain.pojo.Category;
import com.pain.blue.wiki.domain.pojo.CategoryExample;
import com.pain.blue.wiki.mapper.CategoryMapper;
import com.pain.blue.wiki.request.category.CategorySaveRequest;
import com.pain.blue.wiki.request.category.CategoryUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final IdGenerator idGenerator;

    public List<CategoryDTO> index() {
        CategoryExample example = new CategoryExample();
        example.setOrderByClause("sort asc");
        List<Category> categories = categoryMapper.selectByExample(example);
        return CopyUtils.copy(categories, CategoryDTO.class);
    }

    public void save(CategorySaveRequest saveRequest) {
        Category category = CopyUtils.copy(saveRequest, Category.class);
        category.setId(idGenerator.gen());
        categoryMapper.insert(category);
    }

    public void update(Long id, CategoryUpdateRequest updateRequest) {
        updateRequest.setId(id);
        Category category = CopyUtils.copy(updateRequest, Category.class);
        categoryMapper.updateByPrimaryKey(category);
    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
