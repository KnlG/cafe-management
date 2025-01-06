package com.cafe.cafe_management.RESTImpl;

import com.cafe.cafe_management.REST.DashboardREST;
import com.cafe.cafe_management.Service.DashboardService;
import com.cafe.cafe_management.constants.CafeConstants;
import com.cafe.cafe_management.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DashboardRESTImpl implements DashboardREST {

    DashboardService dashboardService;

    @Autowired
    public DashboardRESTImpl(DashboardService dashboardService){
        this.dashboardService = dashboardService;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        try{
            return dashboardService.getCount();
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
