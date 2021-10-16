package com.pain.blue.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pain.blue.id.IdGenerator;
import com.pain.blue.mapping.CopyUtils;
import com.pain.blue.rest.response.PageResult;
import com.pain.blue.wiki.domain.dto.CategoryDTO;
import com.pain.blue.wiki.domain.pojo.Category;
import com.pain.blue.wiki.domain.pojo.CategoryExample;
import com.pain.blue.wiki.mapper.CategoryMapper;
import com.pain.blue.wiki.request.category.CategoryIndexRequest;
import com.pain.blue.wiki.request.category.CategorySaveRequest;
import com.pain.blue.wiki.request.category.CategoryUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final IdGenerator idGenerator;

    public PageResult<CategoryDTO> index(CategoryIndexRequest query) {
        CategoryExample example = new CategoryExample();
        CategoryExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isBlank(query.getName())) {
            criteria.andNameLike("%" + query.getName() + "%");
        }

        PageHelper.startPage(query.getPage(), query.getSize());
        List<Category> categories = categoryMapper.selectByExample(example);
        PageInfo<Category> pageInfo = new PageInfo<>(categories);

        List<CategoryDTO> categoryDTOList = CopyUtils.copy(categories, CategoryDTO.class);
        PageResult<CategoryDTO> pageResult = new PageResult<>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setPage(pageInfo.getPages());
        pageResult.setRows(categoryDTOList);

        return pageResult;
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
