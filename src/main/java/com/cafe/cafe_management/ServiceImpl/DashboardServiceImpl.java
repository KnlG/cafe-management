package com.cafe.cafe_management.ServiceImpl;

import com.cafe.cafe_management.DAO.BillDAO;
import com.cafe.cafe_management.DAO.CategoryDAO;
import com.cafe.cafe_management.DAO.ProductDAO;
import com.cafe.cafe_management.Model.Product;
import com.cafe.cafe_management.REST.DashboardREST;
import com.cafe.cafe_management.Service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    CategoryDAO categoryDAO;
    ProductDAO productDAO;
    BillDAO billDAO;

    @Autowired
    public DashboardServiceImpl(CategoryDAO categoryDAO,  ProductDAO productDAO, BillDAO billDAO){
        this.categoryDAO = categoryDAO;
        this.productDAO = productDAO;
        this.billDAO = billDAO;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        try{
            Map<String, Object> map = new HashMap<>();
            map.put("category", categoryDAO.count());
            map.put("product", productDAO.count());
            map.put("bill", billDAO.count());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
