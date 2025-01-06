package com.cafe.cafe_management.DAO;

import com.cafe.cafe_management.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDAO extends JpaRepository<Category, Integer> {

    List<Category> getAllCategories();
}
