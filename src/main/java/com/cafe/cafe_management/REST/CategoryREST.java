package com.cafe.cafe_management.REST;

import com.cafe.cafe_management.Model.Category;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/category")
public interface CategoryREST {

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewCategory(@RequestBody(required = true)Map<String, String> requestMap);

    @GetMapping(path = "/get")
    ResponseEntity<List<Category>> getAllCategories(@RequestParam(required = false) String filterValue);

    @PostMapping(path="/update")
    ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String, String> requestMap);
}
