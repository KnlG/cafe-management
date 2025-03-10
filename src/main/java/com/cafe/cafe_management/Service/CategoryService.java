package com.cafe.cafe_management.Service;

import com.cafe.cafe_management.Model.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    ResponseEntity<String> addNewCategory(Map<String, String> requestMap);

    ResponseEntity<List<Category>> getAllCategories(String filterValue);

    ResponseEntity<String> updateCategory(Map<String, String> requestMap);

}
