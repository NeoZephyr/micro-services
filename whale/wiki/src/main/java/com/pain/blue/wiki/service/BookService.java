package com.pain.blue.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pain.blue.mapping.CopyUtils;
import com.pain.blue.rest.response.PageResult;
import com.pain.blue.wiki.domain.dto.BookDTO;
import com.pain.blue.wiki.domain.pojo.Book;
import com.pain.blue.wiki.domain.pojo.BookExample;
import com.pain.blue.wiki.mapper.BookMapper;
import com.pain.blue.wiki.query.BookIndexQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {

    private final BookMapper bookMapper;

    public PageResult<BookDTO> index(BookIndexQuery query) {
        log.info("query: " + query);
        BookExample example = new BookExample();
        BookExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isBlank(query.getName())) {
            criteria.andNameLike("%" + query.getName() + "%");
        }

        if (query.getCategoryId() != null) {
            criteria.andCategoryIdEqualTo(query.getCategoryId());
        }

        PageHelper.startPage(query.getPage(), query.getSize());
        List<Book> books = bookMapper.selectByExample(example);
        PageInfo<Book> pageInfo = new PageInfo<>(books);
        List<BookDTO> bookDTOList = CopyUtils.copy(books, BookDTO.class);
        PageResult<BookDTO> pageResult = new PageResult<>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setPage(pageInfo.getPages());
        pageResult.setRows(bookDTOList);

        return pageResult;
    }
}
