package com.cafe.cafe_management.ServiceImpl;

import com.cafe.cafe_management.DAO.CategoryDAO;
import com.cafe.cafe_management.DAO.ProductDAO;
import com.cafe.cafe_management.JWT.JWTFilter;
import com.cafe.cafe_management.Model.Category;
import com.cafe.cafe_management.Model.Product;
import com.cafe.cafe_management.Service.ProductService;
import com.cafe.cafe_management.constants.CafeConstants;
import com.cafe.cafe_management.utils.CafeUtils;
import com.cafe.cafe_management.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    ProductDAO productDAO;
    JWTFilter jwtFilter;
    CategoryDAO categoryDAO;

    @Autowired
    public ProductServiceImpl(ProductDAO productDAO, JWTFilter jwtFilter, CategoryDAO categoryDAO){
        this.productDAO = productDAO;
        this.jwtFilter =  jwtFilter;
        this.categoryDAO = categoryDAO;
    }

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateProductMap(requestMap, false)){
                    productDAO.save(getProductFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Product added successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProducts() {
        try{
            return new ResponseEntity<>(productDAO.getAllProducts(), HttpStatus.OK);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateProductMap(requestMap, true)) {
                    Optional<Product> optional = productDAO.findById(Integer.parseInt(requestMap.get("id")));
                    if(optional.isPresent()){
                        Product product = getProductFromMap(requestMap, true);
                        product.setStatus(optional.get().getStatus());
                        productDAO.save(product);
                        return CafeUtils.getResponseEntity("Product updated successfully.", HttpStatus.OK);
                    }
                    return CafeUtils.getResponseEntity("Product with this id doesn't exist.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try{
            if(jwtFilter.isAdmin()){
                    Optional<Product> optional = productDAO.findById(id);
                    if(optional.isPresent()){
                        productDAO.deleteById(id);
                        return CafeUtils.getResponseEntity("Product deleted successfully.", HttpStatus.OK);
                    }
                    return CafeUtils.getResponseEntity("Product with this id doesn't exist.", HttpStatus.OK);
                }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                Optional<Product> optional = productDAO.findById(Integer.parseInt(requestMap.get("id")));
                if(optional.isPresent()){
                    productDAO.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    return CafeUtils.getResponseEntity("Status updated successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Product with this id doesn't exist.", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try{
            return new ResponseEntity<>(productDAO.getProductsByCategory(id), HttpStatus.OK);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getById(Integer id) {
        try{
                return new ResponseEntity<>(productDAO.getProductById(id), HttpStatus.OK);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean setId) {
        Product product = new Product();
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));
        if(setId){
            product.setId(Integer.parseInt(requestMap.get("id")));
        }else{
            product.setStatus("true");
        }
        product.setCategory(category);
        return product;
    }

    private boolean validateProductMap(Map<String, String> requestMap,  boolean validateId) {
        boolean val = true;
        if (validateId){
            val = requestMap.containsKey("id");
        }
        return val&&requestMap.containsKey("name")&&requestMap.containsKey("categoryId")&&requestMap.containsKey("price");
    }
}
