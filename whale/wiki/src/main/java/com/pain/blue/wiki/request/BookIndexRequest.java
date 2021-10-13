package com.pain.blue.wiki.request;

public class BookIndexRequest extends PageRequest {
    private Long id;
    private String name;
    private Long categoryId;
    private Long subCategoryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    @Override
    public String toString() {
        return "BookIndexQuery{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", subCategoryId=" + subCategoryId +
                "} " + super.toString();
    }
}
