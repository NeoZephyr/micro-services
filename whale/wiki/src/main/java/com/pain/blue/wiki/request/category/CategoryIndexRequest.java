package com.pain.blue.wiki.request.category;

import com.pain.blue.wiki.request.PageRequest;
import lombok.Data;

@Data
public class CategoryIndexRequest extends PageRequest {
    private String name;
}
