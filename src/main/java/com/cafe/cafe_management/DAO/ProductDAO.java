package com.cafe.cafe_management.DAO;

import com.cafe.cafe_management.Model.Product;
import com.cafe.cafe_management.wrapper.ProductWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {

    List<ProductWrapper> getAllProducts();

    @Modifying
    @Transactional
    void updateProductStatus(@Param("status") String status, @Param("id") Integer id);

    List<ProductWrapper> getProductsByCategory(Integer id);

    ProductWrapper getProductById(Integer id);
}
