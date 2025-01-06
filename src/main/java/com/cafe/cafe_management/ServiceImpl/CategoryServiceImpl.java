package com.cafe.cafe_management.ServiceImpl;

import com.cafe.cafe_management.DAO.CategoryDAO;
import com.cafe.cafe_management.JWT.JWTFilter;
import com.cafe.cafe_management.Model.Category;
import com.cafe.cafe_management.Service.CategoryService;
import com.cafe.cafe_management.constants.CafeConstants;
import com.cafe.cafe_management.utils.CafeUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    CategoryDAO categoryDAO;
    JWTFilter jwtFilter;

    @Autowired
    public CategoryServiceImpl(CategoryDAO categoryDAO, JWTFilter jwtFilter){
        this.categoryDAO = categoryDAO;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap){
        try{
            if(jwtFilter.isAdmin()){
                if(validateCategoryMap(requestMap, false)){
                    categoryDAO.save(getCategoryFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Category added successfully.", HttpStatus.OK);
                }
            }else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategories(String filterValue) {
        try {
            if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                log.info("Inside filter.");
                return new ResponseEntity<List<Category>>(categoryDAO.getAllCategories(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryDAO.findAll(), HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateCategoryMap(requestMap, true)){
                    Optional<Category> optional = categoryDAO.findById(Integer.parseInt(requestMap.get("id")));
                    if(optional.isPresent()){
                        categoryDAO.save(getCategoryFromMap(requestMap, true));
                        return CafeUtils.getResponseEntity("Category updated successfully.", HttpStatus.OK);
                    }else{
                        return CafeUtils.getResponseEntity("Category id doesn't exist.", HttpStatus.OK);
                    }
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }
        catch(Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        boolean val = true;
        if(validateId){
            val =  requestMap.containsKey("id");
        }
        return val && requestMap.containsKey("name");
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, boolean setId){
        Category category = new Category();
        if(setId){
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }

}
