package com.pain.blue.wiki.service;

import com.pain.blue.id.IdGenerator;
import com.pain.blue.mapping.CopyUtils;
import com.pain.blue.wiki.domain.dto.CategoryDTO;
import com.pain.blue.wiki.domain.dto.DocumentDTO;
import com.pain.blue.wiki.domain.pojo.Category;
import com.pain.blue.wiki.domain.pojo.CategoryExample;
import com.pain.blue.wiki.domain.pojo.Document;
import com.pain.blue.wiki.domain.pojo.DocumentExample;
import com.pain.blue.wiki.mapper.CategoryMapper;
import com.pain.blue.wiki.mapper.DocumentMapper;
import com.pain.blue.wiki.request.category.CategorySaveRequest;
import com.pain.blue.wiki.request.category.CategoryUpdateRequest;
import com.pain.blue.wiki.request.document.DocumentSaveRequest;
import com.pain.blue.wiki.request.document.DocumentUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentService {

    private final DocumentMapper documentMapper;
    private final IdGenerator idGenerator;

    public List<DocumentDTO> index() {
        DocumentExample example = new DocumentExample();
        example.setOrderByClause("sort asc");
        List<Document> documents = documentMapper.selectByExample(example);
        return CopyUtils.copy(documents, DocumentDTO.class);
    }

    public void save(DocumentSaveRequest saveRequest) {
        Document document = CopyUtils.copy(saveRequest, Document.class);
        document.setId(idGenerator.gen());
        documentMapper.insert(document);
    }

    public void update(Long id, DocumentUpdateRequest updateRequest) {
        updateRequest.setId(id);
        Document document = CopyUtils.copy(updateRequest, Document.class);
        documentMapper.updateByPrimaryKey(document);
    }

    public void delete(Long id) {
        documentMapper.deleteByPrimaryKey(id);
    }
}
