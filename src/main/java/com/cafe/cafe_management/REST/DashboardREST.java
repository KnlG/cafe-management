package com.cafe.cafe_management.REST;

import org.hibernate.engine.spi.Resolution;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "/dashboard")
public interface DashboardREST {

    @GetMapping(path = "/details")
    ResponseEntity<Map<String, Object>> getCount();
}
