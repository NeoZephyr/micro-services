package com.pain.blue.wiki.service;

import com.pain.blue.id.IdGenerator;
import com.pain.blue.mapping.CopyUtils;
import com.pain.blue.wiki.bean.DocumentNode;
import com.pain.blue.wiki.domain.dto.CategoryDTO;
import com.pain.blue.wiki.domain.dto.DocumentDTO;
import com.pain.blue.wiki.domain.pojo.*;
import com.pain.blue.wiki.mapper.CategoryMapper;
import com.pain.blue.wiki.mapper.ContentMapper;
import com.pain.blue.wiki.mapper.DocumentMapper;
import com.pain.blue.wiki.request.category.CategorySaveRequest;
import com.pain.blue.wiki.request.category.CategoryUpdateRequest;
import com.pain.blue.wiki.request.document.DocumentSaveRequest;
import com.pain.blue.wiki.request.document.DocumentUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentService {

    private final DocumentMapper documentMapper;
    private final ContentMapper contentMapper;
    private final IdGenerator idGenerator;

    public List<DocumentDTO> index(long bookId) {
        DocumentExample example = new DocumentExample();
        example.setOrderByClause("sort asc");
        DocumentExample.Criteria criteria = example.createCriteria();
        criteria.andBookIdEqualTo(bookId);
        List<Document> documents = documentMapper.selectByExample(example);
        return CopyUtils.copy(documents, DocumentDTO.class);
    }

    public void save(DocumentSaveRequest saveRequest) {
        Document document = CopyUtils.copy(saveRequest, Document.class);
        Content content = CopyUtils.copy(saveRequest, Content.class);
        document.setId(idGenerator.gen());
        content.setId(document.getId());
        documentMapper.insert(document);
        contentMapper.insert(content);
    }

    public void update(Long id, DocumentUpdateRequest updateRequest) {
        updateRequest.setId(id);
        Document document = CopyUtils.copy(updateRequest, Document.class);
        documentMapper.updateByPrimaryKey(document);

        Content content = contentMapper.selectByPrimaryKey(id);

        if (content == null) {
            content = CopyUtils.copy(updateRequest, Content.class);
            content.setId(id);
            contentMapper.insert(content);
        } else {
            content.setContent(updateRequest.getContent());
            contentMapper.updateByPrimaryKeyWithBLOBs(content);
        }
    }

    public void delete(Long id) {
        Document document = documentMapper.selectByPrimaryKey(id);
        Long bookId = document.getBookId();
        List<DocumentDTO> documents = index(bookId);
        List<DocumentNode> documentNodes = new ArrayList<>();
        Map<Long, DocumentNode> idToNode = new HashMap<>();

        documents.forEach(documentDTO -> {
            DocumentNode documentNode = new DocumentNode();
            documentNode.setDocumentId(documentDTO.getId());
            documentNode.setParent(documentDTO.getParent());
            idToNode.put(documentDTO.getId(), documentNode);
            documentNodes.add(documentNode);
        });

        documentNodes.forEach(documentNode -> {
            long parentId = documentNode.getParent();

            if (parentId != 0) {
                DocumentNode parent = idToNode.get(parentId);
                parent.addChild(documentNode);
            }
        });

        List<Long> ids = new ArrayList<>();
        collectDocumentIds(idToNode.get(id), ids);

        DocumentExample example = new DocumentExample();
        DocumentExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(ids);
        documentMapper.deleteByExample(example);
    }

    public String getContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);

        if (content == null || StringUtils.isBlank(content.getContent())) {
            return "";
        }

        return content.getContent();
    }

    private void collectDocumentIds(DocumentNode documentNode, List<Long> ids) {
        ids.add(documentNode.getDocumentId());

        documentNode.getChildren().forEach(child -> {
            collectDocumentIds(child, ids);
        });
    }
}
