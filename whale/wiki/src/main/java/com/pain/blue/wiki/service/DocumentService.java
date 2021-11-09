package com.pain.blue.wiki.service;

import com.pain.blue.id.IdGenerator;
import com.pain.blue.mapping.CopyUtils;
import com.pain.blue.wiki.bean.DocumentNode;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentService {

    private final DocumentMapper documentMapper;
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
        document.setId(idGenerator.gen());
        documentMapper.insert(document);
    }

    public void update(Long id, DocumentUpdateRequest updateRequest) {
        updateRequest.setId(id);
        Document document = CopyUtils.copy(updateRequest, Document.class);
        documentMapper.updateByPrimaryKey(document);
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

    private void collectDocumentIds(DocumentNode documentNode, List<Long> ids) {
        ids.add(documentNode.getDocumentId());

        documentNode.getChildren().forEach(child -> {
            collectDocumentIds(child, ids);
        });
    }
}
