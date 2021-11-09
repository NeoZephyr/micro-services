package com.pain.blue.wiki.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DocumentNode {
    private long documentId;
    private long parent;
    private List<DocumentNode> children = new ArrayList<>();

    public void addChild(DocumentNode child) {
        children.add(child);
    }
}
